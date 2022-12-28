package ru.practicum.stats.model.hit;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HitDto {
    // Идентификатор записи.
    private Long id;

    // Идентификатор сервиса для которого записывается информация.
    private String app;

    // URI для которого был осуществлен запрос.
    private String uri;

    // IP-адрес пользователя, осуществившего запрос.
    private String ip;

    // Дата и время, когда был совершен запрос к эндпоинту.
    private String hitDate;
}
