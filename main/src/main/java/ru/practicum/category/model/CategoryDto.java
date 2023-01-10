package ru.practicum.category.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    // Идентификатор категории.
    private Long id;

    // Название категории.
    @NotBlank(message = "Не указано название категории!")
    private String name;
}