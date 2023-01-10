package ru.practicum.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.event.model.event.EndpointHit;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

@Component
public class HttpClient {
    private final RestTemplate template;
    private final DateUtility dateUtility;

    public HttpClient(@Value("http://localhost:9090") String url,
                      RestTemplateBuilder template,
                      DateUtility dateUtility) {
        this.dateUtility = dateUtility;
        this.template = template
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public void addHit(HttpServletRequest request) {
        template.postForEntity("/hit",
                getHttpEntity(makeEndpointHit(request)),
                String.class);
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

    private <T> HttpEntity<T> getHttpEntity(T dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return dto == null ? new HttpEntity<>(headers) : new HttpEntity<>(dto, headers);
    }
}