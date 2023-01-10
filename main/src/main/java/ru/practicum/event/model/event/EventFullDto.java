package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.user.model.UserShortDto;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.event.model.location.Location;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    // Идентификатор мероприятия.
    private Long id;

    // Заголовок мероприятия.
    private String title;

    // Краткое описание мероприятия.
    private String annotation;

    // Категории к которой относится мероприятие.
    private CategoryDto category;

    // Нужно ли оплачивать участие в мероприятии (по умолчанию = false).
    private Boolean paid;

    // Дата и время на которые намечено мероприятие (в формате "yyyy-MM-dd HH:mm:ss").
    private String eventDate;

    // Пользователь, инициатор мероприятия, краткая информация - id и name.
    private UserShortDto initiator;

    // Полное описание мероприятия.
    private String description;

    // Ограничение на количество участников, значение 0 или null - означает отсутствие ограничения.
    private Integer participantLimit;

    // Дата и время публикации мероприятия (в формате "yyyy-MM-dd HH:mm:ss").
    private String publishedOn;

    // Список состояний жизненного цикла мероприятия [PENDING, PUBLISHED, CANCELED].
    private String state;

    // Дата и время создания мероприятия (в формате "yyyy-MM-dd HH:mm:ss").
    private String createdOn;

    // Широта и долгота места проведения мероприятия.
    private Location location;

    // Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором мероприятия.
    // Если false - то будут подтверждаться автоматически.
    private Boolean requestModeration;

    // Количество одобренных заявок на участие в мероприятии.
    private Integer confirmedRequests;

    // Количество просмотрев мероприятия.
    private Integer views;
}