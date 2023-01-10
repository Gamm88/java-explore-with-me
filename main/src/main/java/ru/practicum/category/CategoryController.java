package ru.practicum.category;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.category.model.CategoryDto;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.CategoryNewDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    // Добавление новой категории.
    @PostMapping("/admin/categories")
    public CategoryDto addCategory(@Valid @RequestBody CategoryNewDto categoryNewDto) {
        log.info("CategoryController - добавление новой категории: {}.", categoryNewDto);

        return categoryService.addCategory(categoryNewDto);
    }

    // Получение категорий по списку ИД или всех.
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(value = "ids", required = false) Long[] ids,
                                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                           @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("CategoryController - получение категорий по списку ИД или всех.");

        return categoryService.getCategories(ids, from, size);
    }

    // Получение категории по ИД.
    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable("catId") Long categoryId) {
        log.info("CategoryController - получение категории с ИД: {}.", categoryId);

        return categoryService.getCategory(categoryId);
    }

    // Редактирование категории.
    @PatchMapping("/admin/categories")
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("CategoryController - обновление категории, новое значение: {}.", categoryDto);

        return categoryService.updateCategory(categoryDto);
    }

    // Удаление категории.
    @DeleteMapping("/admin/categories/{catId}")
    public String deleteCategory(@PathVariable("catId") Long categoryId) {
        log.info("CategoryController - удаление категорию по ИД: {}", categoryId);
        categoryService.deleteCategory(categoryId);

        return "Категория удалена.";
    }
}