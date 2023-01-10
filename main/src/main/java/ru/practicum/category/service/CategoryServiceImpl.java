package ru.practicum.category.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.category.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.CategoryRepository;
import ru.practicum.exeptions.NotFoundException;
import ru.practicum.exeptions.DuplicateException;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Основные методы API.
     */

    // Добавление новой категории.
    @Override
    public CategoryDto addCategory(CategoryNewDto categoryDto) {
        checkCategoryNameUnique(categoryDto.getName());
        Category category = categoryRepository.save(CategoryMapper.mapToCategory(categoryDto));
        log.info("CategoryService - добавлена новая категория: {}.", category);

        return CategoryMapper.mapToCategoryDto(category);
    }

    // Получение категорий по списку ИД или всех.
    @Override
    public List<CategoryDto> getCategories(Long[] ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<CategoryDto> categoriesDtoList;

        if (ids == null) {
            categoriesDtoList = CategoryMapper.mapToCategoryDto(categoryRepository.findAll(pageRequest));
        } else {
            categoriesDtoList = CategoryMapper.mapToCategoryDto(categoryRepository.findAllById(Arrays.asList(ids)));
        }
        log.info("CategoryService - предоставлены категории: {}.", categoriesDtoList);

        return categoriesDtoList;
    }

    // Получение категории по ИД.
    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category category = getCategoryOrNotFound(categoryId);
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(category);
        log.info("CategoryService - предоставлена категория: {}.", categoryDto);

        return categoryDto;
    }

    // Редактирование категории.
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = getCategoryOrNotFound(categoryDto.getId());
        String newName = categoryDto.getName();
        checkCategoryNameUnique(newName);

        category.setName(newName);
        categoryRepository.save(category);
        log.info("CategoryService - обновлена категория: {}", category);

        return CategoryMapper.mapToCategoryDto(category);
    }

    // Удаление категории.
    @Override
    public void deleteCategory(Long categoryId) {
        getCategoryOrNotFound(categoryId);
        log.info("CategoryController - удаление категории по ИД: {}", categoryId);
        categoryRepository.deleteById(categoryId);
    }

    /**
     * Вспомогательные методы.
     */

    // Получение категории, если не найден - ошибка 404.
    @Override
    public Category getCategoryOrNotFound(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ИД: " + categoryId + " не найдена!"));
    }

    // Проверка на дублирование по названию категории.
    public void checkCategoryNameUnique(String categoryName) {
        if (categoryRepository.findByName(categoryName) != null) {
            throw new DuplicateException("Категория с названием - " + categoryName + ", уже существует!");
        }
    }
}