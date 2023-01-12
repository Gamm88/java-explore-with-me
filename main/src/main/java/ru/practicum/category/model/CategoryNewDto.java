package ru.practicum.category.model;

import lombok.*;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryNewDto {
    // Название категории.
    @NotBlank(message = "Название категории не может быть пустым!")
    @Size(max = 128, message = "Максимальная длина название категории — 128 символов!")
    private String name;
}
