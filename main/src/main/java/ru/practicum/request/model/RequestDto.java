package ru.practicum.request.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    // Идентификатор запроса.
    private Long id;

    // Идентификатор мероприятия, на которое осуществляется запрос.
    private Long event;

    // Идентификатор пользователя отправивший запрос.
    private Long requester;

    // Дата и время создания заявки, в формате "yyyy-MM-dd HH:mm:ss"
    private String created;

    // Статус запроса [PENDING, CONFIRMED, REJECTED, CANCELED].
    private String status;
}