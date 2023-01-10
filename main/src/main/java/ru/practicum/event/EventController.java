package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.model.event.*;
import ru.practicum.request.model.RequestDto;
import ru.practicum.event.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    /**
     * Публичные эндпоинты, в выдаче должны быть только опубликованные мероприятия.
     */

    // Поиск всех мероприятий, со следующими параметрами:
    // text string - текст для поиска в содержимом аннотации и подробном описании мероприятия;
    // categories array - список id категорий в которых будет вестись поиск;
    // paid boolean - поиск только платных/бесплатных мероприятий;
    // rangeStart string - дата и время не раньше которых должно произойти событие;
    // rangeEnd string - дата и время не позже которых должно произойти событие;
    // onlyAvailable boolean - только мероприятия у которых не исчерпан лимит запросов на участие;
    // sort string - вариант сортировки: по дате мероприятия или по количеству просмотров;
    // from integer - количество мероприятий, которые нужно пропустить для формирования текущего набора;
    // size integer - количество мероприятий в наборе.
    // Дополнительные требования:
    // текстовый поиск без учета регистра букв;
    // если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать мероприятия, которые произойдут позже текущей даты и времени;
    // информация о каждом мероприятии должна включать в себя количество просмотров и количество уже одобренных заявок на участие;
    // информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики.
    @GetMapping("/events")
    public List<EventShortDto> getEventsPublic(EventQueryParams eventQueryParams, HttpServletRequest request) {
        log.info("EventController - получение мероприятий, по параметрам: {}.", eventQueryParams);

        return eventService.getEventsPublic(eventQueryParams, request);
    }

    // Получение мероприятия по ИД.
    @GetMapping("/events/{id}")
    public EventFullDto getEventPublic(@PathVariable(name = "id") Long eventId, HttpServletRequest request) {
        log.info("EventController - получение мероприятия с ИД: {}.", eventId);

        return eventService.getEventPublic(eventId, request);
    }

    /**
     * Приватные эндпоинты, только для пользователей прошедших авторизацию.
     */

    // Добавление нового мероприятия.
    @PostMapping("/users/{userId}/events")
    public EventFullDto addEventUser(@PathVariable("userId") Long userId,
                                     @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("EventController - добавление нового мероприятия: {}.", eventNewDto);

        return eventService.addEventUser(userId, eventNewDto);
    }

    // Получение мероприятий созданных пользователем.
    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsUser(@PathVariable Long userId,
                                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                             int from,
                                             @Positive @RequestParam(value = "size", defaultValue = "10")
                                             int size) {
        log.info("EventController - получение мероприятий созданных пользователем с ИД: {}.", userId);

        return eventService.getEventsUser(userId, from, size);
    }

    // Редактирование мероприятия.
    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEventUser(@PathVariable Long userId,
                                        @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("EventController - обновление мероприятия: {}.", eventNewDto);

        return eventService.updateEventUser(userId, eventNewDto);
    }

    // Получение мероприятия по ИД.
    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventUser(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("EventController - получение мероприятия с ИД: {}.", eventId);

        return eventService.getEventUser(userId, eventId);
    }

    // Отмена мероприятия по ИД.
    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEventUser(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        log.info("EventController - отмена мероприятия с ИД: {}.", eventId);

        return eventService.cancelEventUser(userId, eventId);
    }

    // Получение информации о запросах на участие в мероприятии.
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getEventRequestsUser(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        log.info("EventController - Получение информации о запросах на участие в мероприятии с ИД: {}.", eventId);

        return eventService.getEventRequestsUser(userId, eventId);
    }

    // Подтверждение заявки на участие в мероприятии.
    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequestUser(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long reqId) {
        log.info("EventController - подтверждение заявки с ИД: {}, на участие в мероприятии с ИД: {}.", reqId, eventId);

        return eventService.changeRequestStatusUser(userId, eventId, reqId, true);
    }

    // Отклонение заявки на участие в мероприятии.
    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequestUser(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @PathVariable Long reqId) {
        log.info("EventController - отклонение заявки с ИД: {}, на участие в мероприятии с ИД: {}.", reqId, eventId);

        return eventService.changeRequestStatusUser(userId, eventId, reqId, false);
    }

    /**
     * Административные эндпоинты, только для администраторов сервиса.
     */

    // Эндпоинт возвращает полную информацию обо всех мероприятиях подходящих под переданные условия:
    // users array - список id пользователей, чьи мероприятия нужно найти;
    // states array - список состояний в которых находятся искомые мероприятия;
    // categories array - список id категорий в которых будет вестись поиск;
    // rangeStart string - дата и время не раньше которых должно произойти событие;
    // rangeEnd string - дата и время не позже которых должно произойти событие;
    // from integer - количество мероприятий, которые нужно пропустить для формирования текущего набора;
    // size integer - количество мероприятий в наборе.
    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsAdmin(EventQueryParams eventQueryParams) {
        log.info("EventController - получение мероприятий с параметрами: {}.", eventQueryParams);

        return eventService.getEventsAdmin(eventQueryParams);
    }

    // Редактирование мероприятия. Валидация данных не требуется.
    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId,
                                         @RequestBody EventNewDto eventNewDto) {
        log.info("EventController - редактирование мероприятия с ИД: {}.", eventId);

        return eventService.updateEventAdmin(eventId, eventNewDto);
    }

    // Публикация мероприятия, при этом:
    // дата начала мероприятия должна быть не ранее чем за час от даты публикации;
    // событие должно быть в состоянии ожидания публикации - PENDING.
    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEventAdmin(@PathVariable Long eventId) {
        log.info("EventController - публикация мероприятия с ИД: {}.", eventId);

        return eventService.changeEventState(eventId, true);
    }

    // Отклонение мероприятия, не должно быть опубликовано.
    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEventAdmin(@PathVariable Long eventId) {
        log.info("EventController - отклонение мероприятия с ИД: {}.", eventId);

        return eventService.changeEventState(eventId, false);
    }
}