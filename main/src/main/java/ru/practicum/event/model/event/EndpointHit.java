package ru.practicum.event.model.event;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndpointHit {
    // сервис откуда поступил запрос (например: ewm-main-service).
    private String app;

    // идентификатор ресурса (например: /events/1).
    private String uri;

    // IP адрес, который запросил ресурс (например: 192.163.0.1).
    private String ip;

    // дата запроса (например: 2022-09-06 11:00:23).
    private String hitDate;

    public String toString() {
        return String.format("\n app: %s \n uri: %s \n ip: %s \n hitDate: %s \n", app, uri, ip, hitDate);
    }
}