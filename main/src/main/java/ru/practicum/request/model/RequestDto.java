package ru.practicum.request.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    // Идентификатор запроса
    private Long id;

    // ИД события хранящегося в БД
    @NotBlank(message = "Не может быть пустым")
    private Long event;

    // ИД пользователя хранящегося в БД
    @NotBlank(message = "Не может быть пустым")
    private Long requester;

    // Дата и время создания заявки. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private String created;

    // Статус заявки [PENDING, CONFIRMED, REJECTED, CANCELED]
    private String status;
}
