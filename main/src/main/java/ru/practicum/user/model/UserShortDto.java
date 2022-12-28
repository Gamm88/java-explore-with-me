package ru.practicum.user.model;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    // Идентификатор пользователя (например: 3)
    private Long id;

    // Имя (например: Фёдоров Матвей)
    @NotBlank(message = "Не может быть пустым")
    @Size(max = 128, message = "Максимальная длина — 128 символов")
    private String name;
}
