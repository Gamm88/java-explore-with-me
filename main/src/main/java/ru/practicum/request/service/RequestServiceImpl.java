package ru.practicum.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.event.Event;
import ru.practicum.event.model.event.State;
import ru.practicum.event.service.EventService;
import ru.practicum.exeptions.NotFoundException;
import ru.practicum.exeptions.ValidatorExceptions;
import ru.practicum.request.RequestRepository;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestDto;
import ru.practicum.request.model.RequestMapper;
import ru.practicum.request.model.Status;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class RequestServiceImpl implements RequestService {
    private final EventService eventService;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final RequestRepository requestRepository;

    // Добавление запроса пользователя на участие в событии
    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        User user = userService.getUserOrNotFound(userId);
        Event event = eventService.getEventOrNotFound(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidatorExceptions("Нельзя зарегистрироваться в собственном мероприятии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidatorExceptions("Мероприятие недоступно для участия");
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ValidatorExceptions("Достигнут лимит участников в мероприятии");
        }

        Status status;
        if (event.getRequestModeration()) {
            status = Status.PENDING;
        } else {
            status = Status.CONFIRMED;
        }

        Request request = new Request(null, event, user, LocalDateTime.now(), status);
        requestRepository.save(request);
        eventService.increaseConfirmedRequests(event);

        log.info("RequestService - в БД добавлен запрос: {}, на участие в событии: {}", request, event);

        return requestMapper.mapToRequestDto(request);
    }

    // Получение информации о заявках пользователя на участие в мероприятиях
    @Override
    public List<RequestDto> getRequests(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);

        log.info("RequestService - для пользователя с ИД: {}, " +
                "предоставлен список заявок на участие в мероприятиях: {} ", userId, requests);
        return requestMapper.mapToRequestDto(requests);
    }

    // Отмена запроса пользователя на участие в мероприятии
    @Override
    public RequestDto canceledRequest(Long userId, Long requestId) {
        userService.getUserOrNotFound(userId);
        Request request = getRequestOrNotFound(requestId);

        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidatorExceptions("Отменить можно только свой запрос");
        }
        if (request.getStatus().equals(Status.REJECTED) || request.getStatus().equals(Status.CANCELED)) {
            throw new ValidatorExceptions("Текущий статус запроса недоступен для изменения");
        }

        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        log.info("RequestService - запрос отменён: {} ", request);

        return requestMapper.mapToRequestDto(request);
    }

    // Получение запроса, если не найден - ошибка 404
    @Override
    public Request getRequestOrNotFound(Long requestId) {
        return requestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ИД " + requestId + " не найдено."));
    }

    // Получение всех запросов по ИД события
    @Override
    public List<RequestDto> getAllRequestsByEventId(Long eventId) {
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        log.info("RequestService - по событию с ИД: {}, предоставлен список запросов: {}", eventId, requests);

        return requestMapper.mapToRequestDto(requests);
    }

    // Сохранение заявки на участие в событии
    @Override
    public RequestDto changeStatusAndSaveRequest(Request request, Status status) {
        request.setStatus(status);
        requestRepository.save(request);
        log.info("RequestService - сохранение заявка на участие в мероприятии: {}", request);

        return requestMapper.mapToRequestDto(request);
    }
}
