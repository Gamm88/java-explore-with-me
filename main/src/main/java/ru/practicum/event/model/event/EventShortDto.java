package ru.practicum.event.model.event;

import lombok.*;
import ru.practicum.user.model.UserShortDto;
import ru.practicum.category.model.CategoryDto;

/**
 * Краткая информация о мероприятии для пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
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

    // Количество одобренных заявок на участие в данном событии (например: 5)
    private Integer confirmedRequests;

    // Количество просмотрев события (например: 999)
    private Integer views;
}