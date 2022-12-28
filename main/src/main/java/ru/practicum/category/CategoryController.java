package ru.practicum.category;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import ru.practicum.category.model.CategoryDto;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CategoryController {
    private final CategoryService categoryService;

    // создать категорию
    @PostMapping("/admin/categories")
    public CategoryDto addCategories(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("CategoryController - создание категории: {}", categoryDto);

        return categoryService.addCategory(categoryDto);
    }

    // получить категории по списку ИД или всех
    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@RequestParam(value = "ids", required = false) Long[] ids,
                                           @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") int from,
                                           @Positive @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("CategoryController - получение списка категорий");

        return categoryService.getCategories(ids, from, size);
    }

    // получить категорию по ИД
    @GetMapping("/categories/{catId}")
    public CategoryDto getCategories(@PathVariable("catId") Long categoryId) {
        log.info("CategoryController - получение категории по ИД: {}", categoryId);

        return categoryService.getCategory(categoryId);
    }

    // обновить категорию по ИД
    @PatchMapping("/admin/categories")
    public CategoryDto updateCategories(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("CategoryController - обновление категории с ИД: {}, новое значение: {}",
                categoryDto.getId(), categoryDto);

        return categoryService.updateCategory(categoryDto.getId(), categoryDto);
    }

    // удалить категорию по ИД
    @DeleteMapping("/admin/categories/{catId}")
    public void deleteById(@PathVariable("catId") Long categoryId) {
        log.info("CategoryController - удаление категорию по ИД: {}", categoryId);
        categoryService.deleteById(categoryId);
    }
}