package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.CategoryDto;

import java.util.Collection;

/**
 * API для работы с категориями
 */
public interface PublicCategoryService {
    /**
     * Получение категорий
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return
     */
    Collection<CategoryDto> get(Integer from, Integer size);

    /**
     * Получение информации о категории по её идентификатору
     * @param catId
     * @return
     */
    CategoryDto get(Long catId);
}
