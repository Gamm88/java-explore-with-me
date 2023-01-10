package ru.practicum.request;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.request.model.RequestDto;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    // Добавление нового запроса пользователя на участие в мероприятии.
    @PostMapping
    public RequestDto addRequest(@PathVariable Long userId,
                                 @RequestParam(value = "eventId") Long eventId) {
        log.info("RequestController - добавление нового запроса пользователя с ИД: {}, " +
                "на участие в мероприятии с ИД: {}.", userId, eventId);

        return requestService.addRequest(userId, eventId);
    }

    // Получение заявок пользователя на участие в мероприятиях.
    @GetMapping
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        log.info("RequestController - получение заявок пользователя с ИД: {}, на участие в мероприятиях.", userId);

        return requestService.getRequests(userId);
    }

    // Отмена запроса пользователя на участие в мероприятии.
    @PatchMapping("/{requestId}/cancel")
    public RequestDto canceledRequest(@PathVariable Long userId,
                                      @PathVariable Long requestId) {
        log.info("RequestController - отмена запроса пользователя с ИД: {}, " +
                "на участие в мероприятии с ИД: {}.", userId, requestId);

        return requestService.canceledRequest(userId, requestId);
    }
}