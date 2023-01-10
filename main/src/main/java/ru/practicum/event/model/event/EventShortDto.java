package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.user.model.UserShortDto;
import ru.practicum.category.model.CategoryDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
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

    // Пользователь, инициатор мероприятия.
    private UserShortDto initiator;

    // Количество одобренных заявок на участие в данном мероприятии.
    private Integer confirmedRequests;

    // Количество просмотрев мероприятия.
    private Integer views;
}