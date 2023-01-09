package ru.practicum.components;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.event.EndpointHit;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class HttpClient {
    private final RestTemplate restTemplate = new RestTemplate();
    private final DateUtility dateUtility;

    // Исходящий post запрос в сервис статистики.
    // Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
    // Название сервиса, uri и ip пользователя указаны в теле запроса.
    public void addHit(HttpServletRequest request) {
        restTemplate.postForObject("http://localhost:9090/hit", makeEndpointHit(request), String.class);
    }

    // Создаём экземпляр класса EventHitDto
    private EndpointHit makeEndpointHit(HttpServletRequest request) {
        return EndpointHit.builder()
                .app("ExploreWithMeMain")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .hitDate(dateUtility.dateToString(LocalDateTime.now()))
                .build();
    }
}