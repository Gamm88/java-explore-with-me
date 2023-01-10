package ru.practicum.category;

import ru.practicum.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Поиск категории по названию.
    Category findByName(String categoryName);
}