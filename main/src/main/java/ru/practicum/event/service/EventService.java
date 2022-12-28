package ru.practicum.event.service;

import ru.practicum.event.model.event.*;
import ru.practicum.request.model.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    /**
     * Публичные методы, в выдаче должны быть только опубликованные события.
     */

    // Поиск краткой информации о событиях, по параметрам поиска.
    List<EventShortDto> getAllPublic(EventQueryParams eventQueryParams, HttpServletRequest request);

    // Получение подробной информации о событии, по его ИД.
    EventFullDto getEventByIdPublic(Long eventId, HttpServletRequest request);

    /**
     * Приватные методы, только для пользователей прошедших авторизацию.
     */

    // Создать мероприятие.
    EventFullDto addEventPrivate(Long userId, EventNewDto eventNewDto);

    // Получение событий созданных пользователем
    List<EventShortDto> getAllEventsPrivate(Long userId, int from, int size);

    // Изменение события созданного пользователем
    EventFullDto updateEventPrivate(Long userId, EventNewDto eventNewDto);

    // Получение подробной информации о событии созданного пользователем, по ИД
    EventFullDto getEventByIdPrivate(Long userId, Long eventId);

    // Отмена события созданного пользователем, по ИД
    EventFullDto cancelEventByIdPrivate(Long userId, Long eventId);

    // Получение информации о запросах на участие в событии пользователя, по ИД
    List<RequestDto> getEventRequestsPrivate(Long userId, Long eventId);

    // Подтверждение чужой заявки на участие в событии пользователя, по ДИ
    RequestDto confirmRequestPrivate(Long userId, Long eventId, Long reqId);

    // Отклонение чужой заявки на участие в событии пользователя, по ДИ
    RequestDto rejectRequestPrivate(Long userId, Long eventId, Long reqId);

    /**
     * Административные методы, только для администраторов сервиса.
     */

    // Предоставление полной информацию обо всех событиях подходящих под переданные условия.
    List<EventFullDto> getAllEventsAdmin(EventQueryParams eventQueryParams);

    // Редактирование данных события администратором.
    EventFullDto updateEventAdmin(Long eventId, EventNewDto eventNewDto);

    // Публикация ранее созданного события.
    EventFullDto publishEventAdmin(Long eventId);

    // Отклонение ранее созданного события.
    EventFullDto rejectEventAdmin(Long eventId);

    /**
     * Внутренние методы приложения
     */

    // Получение мероприятие, если не найдено - ошибка 404.
    Event getEventOrNotFound(Long userId);

    // Увеличение кол-ва одобренных заявок на участие в мероприятии.
    void increaseConfirmedRequests(Event event);
}