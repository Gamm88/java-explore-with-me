package ru.practicum.category.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    // Идентификатор категории.
    private Long id;

    // Название категории.
    private String name;
}