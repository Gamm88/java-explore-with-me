package ru.practicum.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.event.model.event.EndpointHit;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;

@Component
public class HttpClient {
    private final RestTemplate restTemplate;
    private final DateUtility dateUtility;

    public HttpClient(@Value("http://localhost:9090") String url,
                      RestTemplateBuilder template,
                      DateUtility dateUtility) {
        this.dateUtility = dateUtility;
        this.restTemplate = template
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    // Исходящий post запрос в сервис статистики.
    // Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
    // Название сервиса, uri и ip пользователя указаны в теле запроса.
    public void addHit(HttpServletRequest request) {
        restTemplate.postForObject("hit", makeEndpointHit(request), String.class);
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