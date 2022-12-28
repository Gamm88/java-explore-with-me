package ru.practicum.compilation.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Модель новой подборки получаемая от пользователя.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationNewDto {
    // Заголовок подборки.
    @NotBlank(message = "Заголовок не может быть пустым!")
    @Size(max = 128, message = "Максимальная длина заголовка — 128 символов!")
    private String title;

    // Закреплена ли подборка на главной странице сайта (по умолчанию - false).
    private Boolean pinned = false;

    // Список идентификаторов событий входящих в подборку.
    @UniqueElements(message = "События в подборке должны быть уникальными!")
    private List<Long> events;
}