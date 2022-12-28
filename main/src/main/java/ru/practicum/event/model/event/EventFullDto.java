package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.user.model.UserShortDto;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.event.model.location.Location;

/**
 * Полная информация о мероприятии для пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    // Идентификатор события (например: 3)
    private Long id;

    // Заголовок события (например: Сплав на байдарках)
    private String title;

    // Краткое описание события (например: Сплав на байдарках похож на полет.)
    private String annotation;

    // Категории к которой относится событие (например: 2)
    private CategoryDto category;

    // Нужно ли оплачивать участие в событии (по умолчанию = false)
    private Boolean paid;

    // Дата и время на которые намечено событие. Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private String eventDate;

    // Пользователь, краткая информация - id и name (например: 3, Фёдоров Матвей)
    private UserShortDto initiator;

    // Полное описание события (например: Сплав на байдарках похож на полет. На спокойной воде — это парение.
    // На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления,
    // феерические эмоции, яркие впечатления.)
    private String description;

    // Ограничение на количество участников. Значение 0 или null - означает отсутствие ограничения
    private Integer participantLimit;

    // Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")
    private String publishedOn;

    // Список состояний жизненного цикла события [PENDING, PUBLISHED, CANCELED]
    private String state;

    // Дата и время создания события (в формате "yyyy-MM-dd HH:mm:ss")
    private String createdOn;

    // Широта и долгота места проведения события
    private Location location;

    // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.
    private Boolean requestModeration;

    // Количество одобренных заявок на участие в данном событии (например: 5)
    private Integer confirmedRequests;

    // Количество просмотрев события (например: 999)
    private Integer views;
}