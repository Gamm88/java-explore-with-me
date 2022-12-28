package ru.practicum.category.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    // Идентификатор категории (например: 3)
    private Long id;

    // Название категории (например: Концерты)
    @NotBlank(message = "Не указано имя категории")
    private String name;
}