package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.event.model.location.Location;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

/**
 * Новое событие создаваемое пользователем
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventNewDto {
    // Идентификатор события (например: 3)
    private Long eventId;

    // Заголовок события (например: Сплав на байдарках)
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(message = "Не может быть меньше 3 и больше 120 символов)", max = 120, min = 3)
    private String title;

    // Краткое описание события (например: Сплав на байдарках похож на полет.)
    @NotBlank(message = "Краткое описание не может быть пустым")
    @Size(message = "Краткое описание не может быть меньше 20 и больше 2000 символов)", max = 2000, min = 20)
    private String annotation;

    // id категории к которой относится событие (например: 2)
    private Long category;

    // Нужно ли оплачивать участие в событии (по умолчанию = false)
    private Boolean paid;

    // Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    @NotBlank(message = "Дата и время события не может быть пустым")
    private String eventDate;

    // Полное описание события (например: Сплав на байдарках похож на полет. На спокойной воде — это парение.
    // На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления,
    // феерические эмоции, яркие впечатления.)
    @NotBlank(message = "Описание не может быть пустым")
    @Size(message = "Описание не может быть меньше 20 и больше 7000 символов)", max = 7000, min = 20)
    private String description;

    // Ограничение на количество участников. Значение 0 или null - означает отсутствие ограничения
    private Integer participantLimit;

    // Широта и долгота места проведения события
    private Location location;

    // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.
    private Boolean requestModeration;
}