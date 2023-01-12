package ru.practicum.request.service;

import ru.practicum.request.model.*;

import java.util.List;

public interface RequestService {
    // Основные методы API:

    // Добавление нового запроса пользователя на участие в мероприятии.
    RequestDto addRequest(Long userId, Long eventId);

    // Получение заявок пользователя на участие в мероприятиях.
    List<RequestDto> getRequests(Long userId);

    // Отмена запроса пользователя на участие в мероприятии.
    RequestDto canceledRequest(Long userId, Long requestId);

    // Вспомогательные методы:

    // Получение запроса, если не найден - ошибка 404.
    Request getRequestOrNotFound(Long requestId);

    // Получение всех запросов на участие, по ИД мероприятия.
    List<RequestDto> getAllRequestsByEventId(Long eventId);
}