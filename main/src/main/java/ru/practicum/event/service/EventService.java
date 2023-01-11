package ru.practicum.event.service;

import ru.practicum.event.model.event.*;
import ru.practicum.request.model.RequestDto;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface EventService {
    // Публичные методы API, в выдаче должны быть только опубликованные мероприятия:

    // Поиск краткой информации о мероприятиях, по параметрам поиска.
    List<EventShortDto> getEventsPublic(EventQueryParams eventQueryParams, HttpServletRequest request);

    // Получение мероприятия по ИД.
    EventFullDto getEventPublic(Long eventId, HttpServletRequest request);

    // Приватные методы API, только для пользователей прошедших авторизацию:

    // Добавление нового мероприятия.
    EventFullDto addEventUser(Long userId, EventNewDto eventNewDto);

    // Получение мероприятий созданных пользователем.
    List<EventShortDto> getEventsUser(Long userId, int from, int size);

    // Редактирование мероприятия.
    EventFullDto updateEventUser(Long userId, EventNewDto eventNewDto);

    // Получение мероприятия по ИД.
    EventFullDto getEventUser(Long userId, Long eventId);

    // Отмена мероприятия по ИД.
    EventFullDto cancelEventUser(Long userId, Long eventId);

    // Получение информации о запросах на участие в мероприятии.
    List<RequestDto> getEventRequestsUser(Long userId, Long eventId);

    // Подтверждение или отклонение заявки на участие в мероприятии.
    RequestDto changeRequestStatusUser(Long userId, Long eventId, Long reqId, boolean status);

    // Административные методы API, только для администраторов сервиса:

    // Поиск полной информации о мероприятиях, по параметрам поиска.
    List<EventFullDto> getEventsAdmin(EventQueryParams eventQueryParams);

    // Редактирование мероприятия.
    EventFullDto updateEventAdmin(Long eventId, EventNewDto eventNewDto);

    // Изменение состояния публикации мероприятия.
    EventFullDto changeEventState(Long eventId, boolean state);

    // Внутренние методы приложения:

    // Получение мероприятие, если не найдено - ошибка 404.
    Event getEventOrNotFound(Long userId);

    // Увеличение кол-ва одобренных заявок на участие в мероприятии.
    void increaseConfirmedRequests(Event event);
}