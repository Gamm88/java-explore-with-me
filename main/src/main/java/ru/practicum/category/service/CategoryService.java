package ru.practicum.category.service;

import ru.practicum.category.model.*;

import java.util.List;

public interface CategoryService {
    // Основные методы API:

    // Добавление новой категории.
    CategoryDto addCategory(CategoryNewDto categoryDto);

    // Получение категорий по списку ИД или всех.
    List<CategoryDto> getCategories(Long[] ids, int from, int size);

    // Получение категории по ИД.
    CategoryDto getCategory(Long categoryId);

    // Редактирование категории.
    CategoryDto updateCategory(CategoryDto categoryDto);

    // Удаление категории.
    void deleteCategory(Long categoryId);

    // Вспомогательные методы:

    // Получение категории, если не найден - ошибка 404.
    Category getCategoryOrNotFound(Long categoryId);
}