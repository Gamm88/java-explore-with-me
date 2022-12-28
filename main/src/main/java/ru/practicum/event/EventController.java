package ru.practicum.event;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.model.event.EventFullDto;
import ru.practicum.event.model.event.EventNewDto;
import ru.practicum.event.model.event.EventShortDto;
import ru.practicum.event.model.event.EventQueryParams;
import ru.practicum.event.service.EventService;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.model.RequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class EventController {
    private final EventService eventService;

    /**
     * Публичные эндпоинты, в выдаче должны быть только опубликованные события.
     */

    // Поиск всех мероприятий, со следующими параметрами:
    // text string - текст для поиска в содержимом аннотации и подробном описании события;
    // categories array - список id категорий в которых будет вестись поиск;
    // paid boolean - поиск только платных/бесплатных событий;
    // rangeStart string - дата и время не раньше которых должно произойти событие;
    // rangeEnd string - дата и время не позже которых должно произойти событие;
    // onlyAvailable boolean - только события у которых не исчерпан лимит запросов на участие;
    // sort string - Вариант сортировки: по дате события или по количеству просмотров;
    // from integer - количество событий, которые нужно пропустить для формирования текущего набора;
    // size integer - количество событий в наборе.
    // Дополнительные требования:
    // текстовый поиск без учета регистра букв;
    // если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени;
    // информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие;
    // информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики.
    @GetMapping("/events")
    public List<EventShortDto> getAllEventsPublic(EventQueryParams eventQueryParams, HttpServletRequest request) {
        log.info("EventController - запрошен список событий с параметрами: {}", eventQueryParams);

        return eventService.getAllPublic(eventQueryParams, request);
    }

    // Получение подробной информации о событии, по его ИД
    @GetMapping("/events/{id}")
    public EventFullDto getEventByIdPublic(@PathVariable(name = "id") Long eventId, HttpServletRequest request) {
        log.info("EventController - получение события по ИД: {}", eventId);

        return eventService.getEventByIdPublic(eventId, request);
    }

    /**
     * Приватные эндпоинты, только для пользователей прошедших авторизацию.
     */

    // Создать событие
    @PostMapping("/users/{userId}/events")
    public EventFullDto addEventPrivate(@PathVariable("userId") Long userId,
                                        @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("EventController - создание события: {}", eventNewDto);

        return eventService.addEventPrivate(userId, eventNewDto);
    }

    // Получение событий созданных пользователем
    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getAllEventsPrivate(@PathVariable Long userId,
                                                   @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                   int from,
                                                   @Positive @RequestParam(value = "size", defaultValue = "10")
                                                   int size) {
        log.info("EventController - получение событий для пользователя с ИД: {}", userId);

        return eventService.getAllEventsPrivate(userId, from, size);
    }

    // Изменение события созданного пользователем
    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEventPrivate(@PathVariable Long userId,
                                           @Valid @RequestBody EventNewDto eventNewDto) {
        log.info("EventController - пользователь с ИД: {}, изменяет событие: {}, ", userId, eventNewDto);

        return eventService.updateEventPrivate(userId, eventNewDto);
    }

    // Получение подробной информации о событии созданного пользователем, по ИД
    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventByIdPrivate(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("EventController - для пользователь с ИД: {}, получение события с ИД: {}", userId, eventId);

        return eventService.getEventByIdPrivate(userId, eventId);
    }

    // Отмена события созданного пользователем, по ИД
    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEventByIdPrivate(@PathVariable Long userId,
                                               @PathVariable Long eventId) {
        log.info("EventController - пользователь с ИД: {}, отменяет событие с ИД: {}", userId, eventId);

        return eventService.cancelEventByIdPrivate(userId, eventId);
    }

    // Получение информации о запросах на участие в событии пользователя, по ИД
    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<RequestDto> getEventRequestsPrivate(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        log.info("EventController - для пользователь с ИД: {}, " +
                "получение запросов на участие его в событии с ИД: {}", userId, eventId);

        return eventService.getEventRequestsPrivate(userId, eventId);
    }

    // Подтверждение чужой заявки на участие в событии пользователя, по ДИ
    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequestPrivate(@PathVariable Long userId,
                                            @PathVariable Long eventId,
                                            @PathVariable Long reqId) {
        log.info("EventController - пользователь с ИД: {}, для события с ИД: {}, " +
                "подтверждает заявку на участие с ИД: {} ", userId, eventId, reqId);

        return eventService.confirmRequestPrivate(userId, eventId, reqId);
    }

    // Отклонение чужой заявки на участие в событии пользователя, по ДИ
    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequestPrivate(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @PathVariable Long reqId) {
        log.info("EventController - пользователь с ИД: {}, для события с ИД: {}, " +
                "отклоняет заявку на участие с ИД: {} ", userId, eventId, reqId);

        return eventService.rejectRequestPrivate(userId, eventId, reqId);
    }

    /**
     * Административные эндпоинты, только для администраторов сервиса.
     */

    // Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия:
    // users array - список id пользователей, чьи события нужно найти;
    // states array - список состояний в которых находятся искомые события;
    // categories array - список id категорий в которых будет вестись поиск;
    // rangeStart string - дата и время не раньше которых должно произойти событие;
    // rangeEnd string - дата и время не позже которых должно произойти событие;
    // from integer - количество событий, которые нужно пропустить для формирования текущего набора;
    // size integer - количество событий в наборе.
    @GetMapping("/admin/events")
    public List<EventFullDto> getAllEventsAdmin(EventQueryParams eventQueryParams) {
        log.info("EventController - запрошен список событий с параметрами: {}", eventQueryParams);

        return eventService.getAllEventsAdmin(eventQueryParams);
    }

    // Редактирование данных любого события администратором. Валидация данных не требуется.
    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId,
                                         @RequestBody EventNewDto eventNewDto) {
        log.info("EventController - изменение события с ИД: {}", eventId);

        return eventService.updateEventAdmin(eventId, eventNewDto);
    }

    // Публикация ранее созданного события, при этом:
    // дата начала события должна быть не ранее чем за час от даты публикации;
    // событие должно быть в состоянии ожидания публикации - PENDING.
    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEventAdmin(@PathVariable Long eventId) {
        log.info("EventController - публикация события с ИД: {}", eventId);

        return eventService.publishEventAdmin(eventId);
    }

    // Отклонение ранее созданного события, не должно быть опубликовано.
    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEventAdmin(@PathVariable Long eventId) {
        log.info("EventController - отклонение публикации события с ИД: {}", eventId);

        return eventService.rejectEventAdmin(eventId);
    }
}