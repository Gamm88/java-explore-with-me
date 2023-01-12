package ru.practicum.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.request.model.*;
import ru.practicum.user.model.User;
import ru.practicum.event.model.event.State;
import ru.practicum.event.model.event.Event;
import ru.practicum.user.service.UserService;
import ru.practicum.request.repository.RequestRepository;
import org.springframework.stereotype.Service;
import ru.practicum.event.service.EventService;
import ru.practicum.exeptions.NotFoundException;
import ru.practicum.exeptions.ValidatorExceptions;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor(onConstructor_ = {@Lazy})
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final EventService eventService;
    private final RequestRepository requestRepository;

    // Основные методы API:

    // Добавление нового запроса пользователя на участие в мероприятии.
    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        User user = userService.getUserOrNotFound(userId);
        Event event = eventService.getEventOrNotFound(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidatorExceptions("Нельзя зарегистрироваться в собственном мероприятии!");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidatorExceptions("Мероприятие недоступно для участия!");
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ValidatorExceptions("Достигнут лимит участников в мероприятии!");
        }

        // Проверяем требует ли мероприятие подтверждение на участие.
        Status status;
        if (event.getRequestModeration()) {
            // Статус - ожидание подтверждения.
            status = Status.PENDING;
        } else {
            // Статус - подтверждено.
            status = Status.CONFIRMED;
        }

        Request request = new Request(null, event, user, LocalDateTime.now(), status);
        requestRepository.save(request);
        eventService.increaseConfirmedRequests(event);
        log.info("RequestService - добавлен новый запрос на участие в мероприятии: {}.", request);

        return RequestMapper.mapToRequestDto(request);
    }

    // Получение заявок пользователя на участие в мероприятиях.
    @Override
    public List<RequestDto> getRequests(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        log.info("RequestService - предоставлен заявки на участие в мероприятиях: {}.", requests);

        return RequestMapper.mapToRequestDto(requests);
    }

    // Отмена запроса пользователя на участие в мероприятии.
    @Override
    public RequestDto canceledRequest(Long userId, Long requestId) {
        userService.getUserOrNotFound(userId);
        Request request = getRequestOrNotFound(requestId);

        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidatorExceptions("Отменить можно только свой запрос!");
        }
        if (request.getStatus().equals(Status.REJECTED) || request.getStatus().equals(Status.CANCELED)) {
            throw new ValidatorExceptions("Запрос уже отменён или отклонён!");
        }

        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        log.info("RequestService - отменён запрос пользователя на участие в мероприятии: {}.", request);

        return RequestMapper.mapToRequestDto(request);
    }

    // Вспомогательные методы:

    // Получение запроса, если не найден - ошибка 404.
    @Override
    public Request getRequestOrNotFound(Long requestId) {
        return requestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ИД: " + requestId + " не найден!"));
    }

    // Получение всех запросов на участие, по ИД мероприятия.
    @Override
    public List<RequestDto> getAllRequestsByEventId(Long eventId) {
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        log.info("RequestService - предоставлен список запросов на участие: {}.", requests);

        return RequestMapper.mapToRequestDto(requests);
    }
}