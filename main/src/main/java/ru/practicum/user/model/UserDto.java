package ru.practicum.user.model;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    // Идентификатор пользователя (например: 3)
    private Long id;

    // Имя (например: Фёдоров Матвей)
    @NotBlank(message = "Не может быть пустым")
    @Size(max = 128, message = "Максимальная длина — 128 символов")
    private String name;

    // Электронная почта (например: petrov.i@practicummail.ru)
    @NotBlank(message = "Не может быть пустой")
    @Email(message = "Электронная почта указан некорректно")
    @Size(max = 128, message = "Максимальная длина — 128 символов")
    private String email;
}