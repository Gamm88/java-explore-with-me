package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.components.DateUtility;
import ru.practicum.components.HttpClient;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.event.*;
import ru.practicum.exeptions.NotFoundException;
import ru.practicum.exeptions.ValidatorExceptions;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestDto;
import ru.practicum.request.model.Status;
import ru.practicum.request.service.RequestService;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final EventMapper eventMapper;
    private final DateUtility dateUtility;
    private final CategoryService categoryService;
    private final RequestService requestService;
    private final HttpClient httpClient;

    /**
     * Публичные методы, в выдаче должны быть только опубликованные события.
     */

    // Поиск краткой информации о событиях, по параметрам поиска.
    @Override
    public List<EventShortDto> getAllPublic(EventQueryParams eventQueryParams, HttpServletRequest request) {
        Sort sort = getSortOrValidatorExceptions(eventQueryParams.getSort());
        Pageable pageable = PageRequest.of(eventQueryParams.getFrom(), eventQueryParams.getSize(), sort);

        Specification<Event> specification = EventSpecifications.getUserSpecifications(eventQueryParams);

        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        log.info("EventService - предоставлен список мероприятий: {} ", events);

        // В каждое событие добавляем +1 просмотр и сохраняем в БД
        events.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(events);
        log.info("EventService - для найденных событий добавлены просмотры");

        // Добавляем в статистику информацию о просмотре списка событий.
        httpClient.addHit(request);
        log.info("EventService - в статистику добавлена информацию об обращении к списку событий: {}", request);

        return eventMapper.mapToEventShortDto(events);
    }

    // Поиск подробной информации о событии, по его ИД.
    @Override
    public EventFullDto getEventByIdPublic(Long eventId, HttpServletRequest request) {
        Event event = getEventOrNotFound(eventId);

        log.info("EventService - предоставлено мероприятие: {} ", event);

        // В событие добавляем +1 просмотр и сохраняем в БД
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        log.info("EventService - для найденного события добавлен просмотр");

        // Добавляем в статистику информацию по запросу о подробной информации события.
        httpClient.addHit(request);
        log.info("EventService - в статистику добавлена информацию по запросу о подробной информации события: {}", request);

        return eventMapper.mapToEventFullDto(event);
    }

    /**
     * Приватные методы, только для пользователей прошедших авторизацию.
     */

    // Добавление события
    @Override
    public EventFullDto addEventPrivate(Long userId, EventNewDto eventNewDto) {
        User user = userService.getUserOrNotFound(userId);
        dateUtility.checkEventDateIsBeforeTwoHours(eventNewDto.getEventDate());
        Category category = categoryService.getCategoryOrNotFound(eventNewDto.getCategory());

        Event event = eventRepository.save(eventMapper.mapToEvent(user, eventNewDto, category));
        log.info("EventService - в базу добавлено событие: {} ", event);

        return eventMapper.mapToEventFullDto(event);
    }

    // Получение событий созданных пользователем
    @Override
    public List<EventShortDto> getAllEventsPrivate(Long userId, int from, int size) {
        userService.getUserOrNotFound(userId);
        Pageable pageable = PageRequest.of(from, size);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        log.info("EventService - для пользователя с ИД: {}, предоставлен список события  {}", userId, events);

        return eventMapper.mapToEventShortDto(events);
    }

    // Изменение события созданного пользователем
    @Override
    public EventFullDto updateEventPrivate(Long userId, EventNewDto eventNewDto) {
        Event event = getEventOrNotFound(eventNewDto.getEventId());
        checkStateIsNotPublished(event.getState());
        checkUserIsInitiator(userId, event.getInitiator().getId());

        updateEvent(eventNewDto, event);
        if (event.getState() == State.CANCELED) {
            event.setState(State.PENDING);
        }
        eventRepository.save(event);
        log.info("EventService - обновлено событие {}", event);

        return eventMapper.mapToEventFullDto(event);
    }

    // Получение подробной информации о событии созданного пользователем, по ИД
    @Override
    public EventFullDto getEventByIdPrivate(Long userId, Long eventId) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);

        log.info("EventService - предоставлено мероприятие: {} ", event);

        return eventMapper.mapToEventFullDto(event);
    }

    // Отмена события созданного пользователем, по ИД
    @Override
    public EventFullDto cancelEventByIdPrivate(Long userId, Long eventId) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);
        checkStateIsNotPublished(event.getState());
        checkUserIsInitiator(userId, event.getInitiator().getId());
        checkStateIsPending(event.getState());

        event.setState(State.CANCELED);
        eventRepository.save(event);
        log.info("EventService - отменено событие: {}", event);

        return eventMapper.mapToEventFullDto(event);
    }

    // Получение информации о запросах на участие в событии пользователя, по ИД
    @Override
    public List<RequestDto> getEventRequestsPrivate(Long userId, Long eventId) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);
        checkUserIsInitiator(userId, event.getInitiator().getId());

        List<RequestDto> requests = requestService.getAllRequestsByEventId(eventId);
        log.info("EventService - по событию с ИД: {}, предоставлен список запросов: {}", eventId, requests);

        return requests;
    }

    // Подтверждение чужой заявки на участие в событии пользователя, по ИД
    @Override
    public RequestDto confirmRequestPrivate(Long userId, Long eventId, Long reqId) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);
        Request request = requestService.getRequestOrNotFound(reqId);

        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ValidatorExceptions("Достигнут лимит участников в событии");
        }

        RequestDto requestDto = requestService.changeRequestStatus(request, Status.CONFIRMED);
        increaseConfirmedRequests(event);
        log.info("EventService - подтверждена заявка на участие в мероприятии: {}", request);

        return requestDto;
    }

    // Отклонение чужой заявки на участие в событии пользователя, по ИД
    @Override
    public RequestDto rejectRequestPrivate(Long userId, Long eventId, Long reqId) {
        userService.getUserOrNotFound(userId);
        getEventOrNotFound(eventId);
        Request request = requestService.getRequestOrNotFound(reqId);

        RequestDto requestDto = requestService.changeRequestStatus(request, Status.REJECTED);
        log.info("EventService - отклонена заявка на участие в мероприятии: {}", requestDto);

        return requestDto;
    }

    /**
     * Административные методы, только для администраторов сервиса
     */

    // Предоставление полной информацию обо всех событиях подходящих под переданные условия.
    @Override
    public List<EventFullDto> getAllEventsAdmin(EventQueryParams eventQueryParams) {
        Pageable pageable = PageRequest.of(eventQueryParams.getFrom(), eventQueryParams.getSize());

        Specification<Event> specification = EventSpecifications.getAdminSpecifications(eventQueryParams);

        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        log.info("EventService - предоставлен список мероприятий: {} ", events);

        return eventMapper.mapToEventFullDto(events);
    }

    // Редактирование данных события администратором.
    @Override
    public EventFullDto updateEventAdmin(Long eventId, EventNewDto eventNewDto) {
        Event event = getEventOrNotFound(eventId);
        updateEvent(eventNewDto, event);

        Optional.ofNullable(eventNewDto.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(eventNewDto.getRequestModeration()).ifPresent(event::setRequestModeration);

        eventRepository.save(event);
        log.info("EventService - обновлено событие {}", event);

        return eventMapper.mapToEventFullDto(event);
    }

    // Публикация ранее созданного события, при этом:
    // дата начала события должна быть не ранее чем за час от даты публикации (текущего времени);
    // событие должно быть в состоянии ожидания публикации - PENDING.
    @Override
    public EventFullDto publishEventAdmin(Long eventId) {
        Event event = getEventOrNotFound(eventId);
        dateUtility.checkEventDateIsBeforeOneHours(event.getEventDate());
        checkStateIsPending(event.getState());

        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PUBLISHED);
        eventRepository.save(event);
        log.info("EventService - опубликовано событие: {}", event);

        return eventMapper.mapToEventFullDto(event);
    }

    // Отклонение ранее созданного события.
    @Override
    public EventFullDto rejectEventAdmin(Long eventId) {
        Event event = getEventOrNotFound(eventId);
        checkStateIsPending(event.getState());

        event.setState(State.CANCELED);
        eventRepository.save(event);
        log.info("EventService - отклонено событие: {}", event);

        return eventMapper.mapToEventFullDto(event);
    }

    /**
     * Вспомогательные методы.
     */

    // Получение события, если не найдено - ошибка 404
    @Override
    public Event getEventOrNotFound(Long eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с ИД " + eventId + " не найдено."));
    }

    // Увеличение кол-ва одобренных заявок на участие в мероприятии
    @Override
    public void increaseConfirmedRequests(Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
    }

    // Проверка, что текущий пользователь является инициатором мероприятия
    public void checkUserIsInitiator(Long userId, Long initiatorId) {
        if (!userId.equals(initiatorId)) {
            throw new ValidatorExceptions("У вас нет доступа к редактированию этого события!");
        }
    }

    // Проверка статуса на ожидание публикации
    public void checkStateIsPending(State checkingState) {
        if (checkingState != State.PENDING) {
            throw new ValidatorExceptions("Событие не может быть опубликовано, статус события не ожидает публикации");
        }
    }

    // Проверка статуса на отменено
    public void checkStateIsNotPublished(State checkingState) {
        if (checkingState == State.PUBLISHED) {
            throw new ValidatorExceptions("Событие не может быть изменено, так как уже опубликовано!");
        }
    }

    // Получение параметра сортировки из запроса, проверка на валидацию
    private Sort getSortOrValidatorExceptions(String sort) {
        if (sort == null || sort.equals("EVENT_DATE")) {
            sort = "eventDate";
            return Sort.by(Sort.Direction.DESC, sort);
        } else if (sort.equals("VIEWS")) {
            sort = "views";
            return Sort.by(Sort.Direction.DESC, sort);
        } else {
            throw new ValidatorExceptions("Формат сортировки " + sort + " не поддерживается.");
        }
    }

    // Обновление события
    private void updateEvent(EventNewDto eventNewDto, Event event) {
        if (eventNewDto.getAnnotation() != null) {
            event.setAnnotation(eventNewDto.getAnnotation());
        }
            event.setCategory(categoryService.getCategoryOrNotFound(eventNewDto.getCategory()));
        if (eventNewDto.getDescription() != null) {
            event.setDescription(eventNewDto.getDescription());
        }
        if (eventNewDto.getEventDate() != null) {
            dateUtility.checkEventDateIsBeforeTwoHours(eventNewDto.getEventDate());
            event.setEventDate(dateUtility.stringToDate(eventNewDto.getEventDate()));
        }
        if (eventNewDto.getPaid() != null) {
            event.setPaid(eventNewDto.getPaid());
        }
        if (eventNewDto.getParticipantLimit() != 0) {
            event.setParticipantLimit(eventNewDto.getParticipantLimit());
        }
        if (eventNewDto.getTitle() != null) {
            event.setTitle(eventNewDto.getTitle());
        }
    }
}