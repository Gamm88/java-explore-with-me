package ru.practicum.user.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    // Идентификатор пользователя.
    private Long id;

    // Имя пользователя.
    private String name;
}