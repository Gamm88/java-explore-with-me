package ru.practicum.category.model;

import java.util.List;
import java.util.ArrayList;

public class CategoryMapper {
    //из newCategoryDto в Category
    public static Category mapToCategory(CategoryNewDto categoryNewDto) {
        return Category.builder()
                .name(categoryNewDto.getName())
                .build();
    }

    //из Category в CategoryDto
    public static CategoryDto mapToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    //получение списка CategoriesDto из списка Categories
    public static List<CategoryDto> mapToCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> dtos = new ArrayList<>();
        for (Category category : categories) {
            dtos.add(mapToCategoryDto(category));
        }
        return dtos;
    }
}