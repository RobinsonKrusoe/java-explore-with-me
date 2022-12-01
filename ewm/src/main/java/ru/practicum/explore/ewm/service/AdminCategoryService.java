package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.CategoryDto;
import ru.practicum.explore.ewm.dto.NewCategoryDto;
import ru.practicum.explore.ewm.model.Category;

/**
 * API для работы с категориями
 */
public interface AdminCategoryService {
    /**
     * Добавление новой категории
     * @param newCategoryDto
     * @return
     */
    CategoryDto add(NewCategoryDto newCategoryDto);

    /**
     * Изменение категории
     * @param categoryDto
     * @return
     */
    CategoryDto patch(CategoryDto categoryDto);

    /**
     * Удаление категории
     */
    void del(long id);

    Category get(long id);
}
