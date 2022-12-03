package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.CategoryDto;
import ru.practicum.explore.ewm.dto.NewCategoryDto;
import ru.practicum.explore.ewm.service.AdminCategoryService;

import javax.validation.Valid;

/**
 * Контроллер API для работы с категориями
 */
@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
public class AdminCategoriesController {
    private final AdminCategoryService service;

    public AdminCategoriesController(AdminCategoryService service) {
        this.service = service;
    }

    /**
     * Изменение категории
     * @param categoryDto
     * @return
     */
    @PatchMapping
    public CategoryDto patchCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Patch categoryDto {}", categoryDto);
        return service.patch(categoryDto);
    }

    /**
     * Добавление новой категории
     * @param categoryDto
     * @return
     */
    @PostMapping
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("Post categoryDto {}", categoryDto);
        return service.add(categoryDto);
    }

    /**
     * Удаление категории
     * @param catId
     */
    @DeleteMapping("/{catId}")
    public void delCategory(@PathVariable long catId) {
        log.info("Delete userId={}", catId);
        service.del(catId);
    }
}
