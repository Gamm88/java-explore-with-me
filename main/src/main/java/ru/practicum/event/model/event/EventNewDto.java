package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.event.model.location.Location;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventNewDto {
    // Идентификатор мероприятия.
    private Long eventId;

    // Заголовок мероприятия.
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(message = "Заголовок не может быть меньше 3 и больше 120 символов)", max = 120, min = 3)
    private String title;

    // Краткое описание мероприятия.
    @NotBlank(message = "Краткое описание не может быть пустым")
    @Size(message = "Краткое описание не может быть меньше 20 и больше 1000 символов)", max = 1000, min = 20)
    private String annotation;

    // id категории к которой относится мероприятие.
    private Long category;

    // Нужно ли оплачивать участие в мероприятии (по умолчанию = false).
    private Boolean paid;

    // Дата и время на которые намечено мероприятие (в формате "yyyy-MM-dd HH:mm:ss").
    @NotBlank(message = "Дата и время мероприятия не может быть пустым")
    private String eventDate;

    // Полное описание мероприятия.
    @NotBlank(message = "Описание не может быть пустым")
    @Size(message = "Описание не может быть меньше 20 и больше 7000 символов)", max = 7000, min = 20)
    private String description;

    // Ограничение на количество участников, значение 0 или null - означает отсутствие ограничения.
    private Integer participantLimit;

    // Место проведения мероприятия.
    private Location location;

    // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором мероприятия.
    // Если false - то будут подтверждаться автоматически.
    private Boolean requestModeration;
}