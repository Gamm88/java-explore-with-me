package ru.practicum.category.model;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

@Component
public class CategoryMapper {
    //из newCategoryDto в Category
    public static Category mapToCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
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