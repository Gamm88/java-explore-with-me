package ru.practicum.compilation.model;

import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationNewDto {
    // Заголовок подборки.
    @NotBlank(message = "Заголовок подборки не может быть пустым!")
    @Size(max = 120, message = "Максимальная длина заголовка подборки — 120 символов!")
    private String title;

    // Закреплена ли подборка на главной странице сайта (по умолчанию - false).
    private Boolean pinned = false;

    // Список идентификаторов мероприятий входящих в подборку.
    @UniqueElements(message = "Мероприятия в подборке должны быть уникальными!")
    private List<Long> events;
}