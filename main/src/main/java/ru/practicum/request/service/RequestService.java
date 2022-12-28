package ru.practicum.request.service;

import ru.practicum.request.model.Request;
import ru.practicum.request.model.RequestDto;
import ru.practicum.request.model.Status;

import java.util.List;

public interface RequestService {
    // Добавление запроса пользователя на участие в мероприятии
    RequestDto addRequest(Long userId, Long eventId);
    // Получение информации о заявках пользователя на участие в мероприятиях
    List<RequestDto> getRequests(Long userId);
    // Отмена запроса пользователя на участие в мероприятии
    RequestDto canceledRequest(Long userId, Long requestId);
    // Получение запроса, если не найден - ошибка 404
    Request getRequestOrNotFound(Long requestId);

    /**
     * Методы сервиса событий
     */
    
    // Получение всех запросов по ИД события
    List<RequestDto> getAllRequestsByEventId(Long eventId);

    // Сохранение заявки на участие в событии, после изменения статуса
    RequestDto changeStatusAndSaveRequest(Request request, Status status);
}