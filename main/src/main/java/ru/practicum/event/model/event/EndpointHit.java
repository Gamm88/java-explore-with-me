package ru.practicum.event.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointHit {
    private String app; // сервис откуда поступил запрос (например: ewm-main-service)
    private String uri; // идентификатор ресурса (например: /events/1)
    private String ip; // IP адрес, который запросил ресурс (например: 192.163.0.1)
    private String hitDate; // дата запроса (например: 2022-09-06 11:00:23)

    public String toString() {
        return String.format("\n app: %s \n uri: %s \n ip: %s \n hitDate: %s \n", app, uri, ip, hitDate);
    }
}
