package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.event.EventShortDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    // Идентификатор подборки.
    private Long id;

    // Заголовок подборки.
    private String title;

    // Закреплена ли подборка на главной странице сайта.
    private Boolean pinned;

    // Список событий входящих в подборку.
    private List<EventShortDto> events;
}
