package ru.practicum.category.service;

import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.model.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    // создать категорию
    CategoryDto addCategory(NewCategoryDto categoryDto);

    // получить категории по списку ИД или всех
    List<CategoryDto> getCategories(Long[] ids, int from, int size);

    // получить категорию по ИД
    CategoryDto getCategory(Long categoryId);

    // обновление категории
    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);

    // удалить категорию по ИД
    void deleteById(Long categoryId);

    // получение категории, если не найден - ошибка 404
    Category getCategoryOrNotFound(Long categoryId);
}