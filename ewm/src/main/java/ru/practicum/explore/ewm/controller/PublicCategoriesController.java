package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.CategoryDto;
import ru.practicum.explore.ewm.service.PublicCategoryService;

import java.util.Collection;

/**
 * Публичный API для работы с категориями
 */
@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class PublicCategoriesController {
    private final PublicCategoryService service;

    public PublicCategoriesController(PublicCategoryService service) {
        this.service = service;
    }

    /**
     * Получение категорий
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return
     */
    @GetMapping
    public Collection<CategoryDto> get(@RequestParam(required = false, defaultValue = "0") Integer from,
                                       @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get Categories with from={}, size={}", from, size);
        return service.get(from, size);
    }

    /**
     * Получение информации о категории по её идентификатору
     * @param catId
     * @return
     */
    @GetMapping("/{catId}")
    public CategoryDto get(@PathVariable Long catId) {
        log.info("Get Category with catId={}", catId);
        return service.get(catId);
    }
}
