package ru.practicum.category.service;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.category.model.Category;
import lombok.RequiredArgsConstructor;
import ru.practicum.category.model.CategoryDto;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.CategoryMapper;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.exeptions.NotFoundException;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    // создать категории
    @Override
    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        //checkCategoryNameUnique(categoryDto.getName());
        Category createdCategory = categoryRepository.save(CategoryMapper.mapToCategory(categoryDto));
        log.info("CategoryService - в базу добавлен категория: {} ", createdCategory);

        return CategoryMapper.mapToCategoryDto(createdCategory);
    }

    // получить категории по списку ИД или всех
    @Override
    public List<CategoryDto> getCategories(Long[] ids, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<CategoryDto> categoriesDtoList;

        if (ids != null) {
            categoriesDtoList = CategoryMapper.mapToCategoryDto(categoryRepository.findAllById(Arrays.asList(ids)));
        } else {
            categoriesDtoList = CategoryMapper.mapToCategoryDto(categoryRepository.findAll(pageRequest));
        }

        log.info("CategoryService - предоставлен список категорий: {} ", categoriesDtoList);

        return categoriesDtoList;
    }

    // получить категорию по ИД
    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category getCategory = getCategoryOrNotFound(categoryId);
        CategoryDto categoryDto = CategoryMapper.mapToCategoryDto(getCategory);
        log.info("CategoryService - по ИД: {} получена категория: {}", categoryId, categoryDto);

        return categoryDto;
    }

    // обновление категории
    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category updatedCategory = getCategoryOrNotFound(categoryId);
        String newName = categoryDto.getName();
        //checkCategoryNameUnique(newName);

        updatedCategory.setName(newName);
        updatedCategory = categoryRepository.save(updatedCategory);
        log.info("CategoryService - в базе обновлена категория: {}", updatedCategory);

        return CategoryMapper.mapToCategoryDto(updatedCategory);
    }

    // удалить категории по ИД
    @Override
    public void deleteById(Long categoryId) {
        getCategoryOrNotFound(categoryId);
        log.info("CategoryController - удаление категории по ИД: {}", categoryId);
        categoryRepository.deleteById(categoryId);
    }

    // получение категории, если не найден - ошибка 404
    @Override
    public Category getCategoryOrNotFound(Long categoryId) {
        return categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с ИД " + categoryId + " не найдена."));
    }

    /*
    // Проверка на дублирование по названию категории.
    public void checkCategoryNameUnique(String categoryName) {
        if (categoryRepository.findByName(categoryName) != null) {
            throw new DuplicateException("Категория - " + categoryName + ", уже существует!");
        }
    }
    */
}