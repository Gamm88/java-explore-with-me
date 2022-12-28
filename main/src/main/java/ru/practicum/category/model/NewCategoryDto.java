package ru.practicum.category.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    // Название категории (например: Концерты)
    @NotBlank(message = "Не может быть пустым")
    @Size(max = 128, message = "Максимальная длина — 128 символов")
    private String name;
}
