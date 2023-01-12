package ru.practicum.user.model;

import lombok.*;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    // Идентификатор пользователя.
    private Long id;

    // Имя пользователя.
    @NotBlank(message = "Имя пользователя не может быть пустым!")
    @Size(max = 128, message = "Максимальная длина имени пользователя — 128 символов!")
    private String name;

    // Электронная почта пользователя.
    @NotBlank(message = "Электронная почта не может быть пустой!")
    @Email(message = "Электронная почта указан некорректно!")
    @Size(max = 128, message = "Максимальная длина электронной почты — 128 символов!")
    private String email;
}