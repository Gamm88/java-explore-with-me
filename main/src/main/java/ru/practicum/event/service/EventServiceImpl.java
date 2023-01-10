package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.request.model.*;
import ru.practicum.user.model.User;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.model.event.*;
import ru.practicum.components.HttpClient;
import ru.practicum.event.EventRepository;
import ru.practicum.components.DateUtility;
import org.springframework.data.domain.Sort;
import ru.practicum.category.model.Category;
import ru.practicum.user.service.UserService;
import ru.practicum.request.RequestRepository;
import org.springframework.stereotype.Service;
import ru.practicum.exeptions.NotFoundException;
import org.springframework.data.domain.Pageable;
import ru.practicum.exeptions.ValidatorExceptions;
import org.springframework.data.domain.PageRequest;
import ru.practicum.request.service.RequestService;
import ru.practicum.category.service.CategoryService;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final HttpClient httpClient;
    private final UserService userService;
    private final DateUtility dateUtility;
    private final RequestService requestService;
    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final RequestRepository requestRepository;

    /**
     * Публичные методы API, в выдаче должны быть только опубликованные мероприятия.
     */

    // Поиск краткой информации о мероприятиях, по параметрам поиска.
    @Override
    public List<EventShortDto> getEventsPublic(EventQueryParams eventQueryParams, HttpServletRequest request) {
        Sort sort = getSortOrValidatorExceptions(eventQueryParams.getSort());
        Pageable pageable = PageRequest.of(eventQueryParams.getFrom(), eventQueryParams.getSize(), sort);

        Specification<Event> specification = EventSpecifications.getUserSpecifications(eventQueryParams);

        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        log.info("EventService - предоставлен список мероприятий: {} ", events);

        // В каждое мероприятие добавляем +1 просмотр и сохраняем в БД
        events.forEach(event -> event.setViews(event.getViews() + 1));
        eventRepository.saveAll(events);
        log.info("EventService - для найденных мероприятий добавлены просмотры");

        // Добавляем в статистику информацию о просмотре списка мероприятий.
        httpClient.addHit(request);
        log.info("EventService - в статистику добавлена информацию об обращении к списку мероприятий: {}", request);

        return EventMapper.mapToEventShortDto(events);
    }

    // Получение мероприятия по ИД.
    @Override
    public EventFullDto getEventPublic(Long eventId, HttpServletRequest request) {
        Event event = getEventOrNotFound(eventId);

        log.info("EventService - предоставлено мероприятие: {} ", event);

        // В мероприятие добавляем +1 просмотр и сохраняем в БД
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        log.info("EventService - для найденного события добавлен просмотр");

        // Добавляем в статистику информацию по запросу о подробной информации события.
        httpClient.addHit(request);
        log.info("EventService - в статистику добавлена информацию по запросу о подробной информации события: {}", request);

        return EventMapper.mapToEventFullDto(event);
    }

    /**
     * Приватные методы, только для пользователей прошедших авторизацию.
     */

    // Добавление события
    @Override
    public EventFullDto addEventUser(Long userId, EventNewDto eventNewDto) {
        User user = userService.getUserOrNotFound(userId);
        dateUtility.checkEventDateIsBeforeTwoHours(eventNewDto.getEventDate());
        Category category = categoryService.getCategoryOrNotFound(eventNewDto.getCategory());

        Event event = eventRepository.save(EventMapper.mapToEvent(user, eventNewDto, category));
        log.info("EventService - в базу добавлено мероприятие: {} ", event);

        return EventMapper.mapToEventFullDto(event);
    }

    // Получение мероприятий созданных пользователем.
    @Override
    public List<EventShortDto> getEventsUser(Long userId, int from, int size) {
        userService.getUserOrNotFound(userId);
        Pageable pageable = PageRequest.of(from, size);

        List<Event> events = eventRepository.findAllByInitiatorId(userId, pageable);
        log.info("EventService - для пользователя с ИД: {}, предоставлен список события  {}", userId, events);

        return EventMapper.mapToEventShortDto(events);
    }

    // Редактирование мероприятия.
    @Override
    public EventFullDto updateEventUser(Long userId, EventNewDto eventNewDto) {
        Event event = getEventOrNotFound(eventNewDto.getId());
        checkStateIsNotPublished(event.getState());
        checkUserIsInitiator(userId, event.getInitiator().getId());

        updateEvent(eventNewDto, event);
        if (event.getState() == State.CANCELED) {
            event.setState(State.PENDING);
        }
        eventRepository.save(event);
        log.info("EventService - обновлено мероприятие {}", event);

        return EventMapper.mapToEventFullDto(event);
    }

    // Получение мероприятия по ИД.
    @Override
    public EventFullDto getEventUser(Long userId, Long eventId) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);

        log.info("EventService - предоставлено мероприятие: {} ", event);

        return EventMapper.mapToEventFullDto(event);
    }

    // Отмена мероприятия по ИД.
    @Override
    public EventFullDto cancelEventUser(Long userId, Long eventId) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);
        checkStateIsNotPublished(event.getState());
        checkUserIsInitiator(userId, event.getInitiator().getId());
        checkStateIsPending(event.getState());

        event.setState(State.CANCELED);
        eventRepository.save(event);
        log.info("EventService - отменено мероприятие: {}", event);

        return EventMapper.mapToEventFullDto(event);
    }

    // Получение информации о запросах на участие в мероприятии.
    @Override
    public List<RequestDto> getEventRequestsUser(Long userId, Long eventId) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);
        checkUserIsInitiator(userId, event.getInitiator().getId());

        List<RequestDto> requests = requestService.getAllRequestsByEventId(eventId);
        log.info("EventService - по событию с ИД: {}, предоставлен список запросов: {}", eventId, requests);

        return requests;
    }

    // Подтверждение или отклонение заявки на участие в мероприятии.
    @Override
    public RequestDto changeRequestStatusUser(Long userId, Long eventId, Long reqId, boolean status) {
        userService.getUserOrNotFound(userId);
        Event event = getEventOrNotFound(eventId);
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ValidatorExceptions("Достигнут лимит участников в мероприятии");
        }
        Request request = requestService.getRequestOrNotFound(reqId);

        if (status) {
            request.setStatus(Status.CONFIRMED);
            requestRepository.save(request);
            increaseConfirmedRequests(event);
            log.info("EventService - подтверждена заявка на участие в мероприятии: {}.", request);
        } else {
            request.setStatus(Status.REJECTED);
            requestRepository.save(request);
            log.info("EventService - отклонена заявка на участие в мероприятии: {}.", request);
        }

        return RequestMapper.mapToRequestDto(request);
    }

    /**
     * Административные методы, только для администраторов сервиса
     */

    // Поиск полной информации о мероприятиях, по параметрам поиска.
    @Override
    public List<EventFullDto> getEventsAdmin(EventQueryParams eventQueryParams) {
        Pageable pageable = PageRequest.of(eventQueryParams.getFrom(), eventQueryParams.getSize());

        Specification<Event> specification = EventSpecifications.getAdminSpecifications(eventQueryParams);

        List<Event> events = eventRepository.findAll(specification, pageable).getContent();
        log.info("EventService - предоставлен список мероприятий: {} ", events);

        return EventMapper.mapToEventFullDto(events);
    }

    // Редактирование мероприятия.
    @Override
    public EventFullDto updateEventAdmin(Long eventId, EventNewDto eventNewDto) {
        Event event = getEventOrNotFound(eventId);
        updateEvent(eventNewDto, event);

        Optional.ofNullable(eventNewDto.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(eventNewDto.getRequestModeration()).ifPresent(event::setRequestModeration);

        eventRepository.save(event);
        log.info("EventService - обновлено мероприятие {}", event);

        return EventMapper.mapToEventFullDto(event);
    }

    // Изменение состояния публикации мероприятия.
    @Override
    public EventFullDto changeEventState(Long eventId, boolean state) {
        Event event = getEventOrNotFound(eventId);
        checkStateIsPending(event.getState());
        dateUtility.checkEventDateIsBeforeOneHours(event.getEventDate());

        if (state) {
            event.setPublishedOn(LocalDateTime.now());
            event.setState(State.PUBLISHED);
            eventRepository.save(event);
            log.info("EventService - опубликовано мероприятие: {}.", event);
        } else {
            event.setState(State.CANCELED);
            eventRepository.save(event);
            log.info("EventService - отклонено мероприятие: {}.", event);
        }

        return EventMapper.mapToEventFullDto(event);
    }

    /**
     * Вспомогательные методы.
     */

    // Получение мероприятие, если не найдено - ошибка 404.
    @Override
    public Event getEventOrNotFound(Long eventId) {
        return eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException("Мероприятие с ИД " + eventId + " не найдено."));
    }

    // Увеличение кол-ва одобренных заявок на участие в мероприятии.
    @Override
    public void increaseConfirmedRequests(Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
    }

    // Проверка, что текущий пользователь является инициатором мероприятия.
    public void checkUserIsInitiator(Long userId, Long initiatorId) {
        if (!userId.equals(initiatorId)) {
            throw new ValidatorExceptions("У вас нет доступа к редактированию этого события!");
        }
    }

    // Проверка статуса на ожидание публикации.
    public void checkStateIsPending(State checkingState) {
        if (checkingState != State.PENDING) {
            throw new ValidatorExceptions("Мероприятие не может быть опубликовано, так как не ожидает публикации!");
        }
    }

    // Проверка статуса на отменено.
    public void checkStateIsNotPublished(State checkingState) {
        if (checkingState == State.PUBLISHED) {
            throw new ValidatorExceptions("Мероприятие не может быть изменено, так как уже опубликовано!");
        }
    }

    // Получение параметра сортировки из запроса, проверка на валидацию.
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

    // Обновление события.
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