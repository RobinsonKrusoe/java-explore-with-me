package ru.practicum.explore.ewm.service;

import org.springframework.stereotype.Component;

import ru.practicum.explore.errorHandle.exception.EntityAlreadyExistException;
import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.CategoryDto;
import ru.practicum.explore.ewm.dto.NewCategoryDto;
import ru.practicum.explore.ewm.mapper.CategoryMapper;
import ru.practicum.explore.ewm.model.Category;
import ru.practicum.explore.ewm.repository.CategoryRepository;

import java.util.Optional;

@Component
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;

    public AdminCategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Добавление новой категории
     *
     * @param newCategoryDto
     * @return
     */
    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null) {
            throw new ValidationException("Пустое иия категории!");
        }

        if (repository.existsByName(newCategoryDto.getName())) {
            throw new EntityAlreadyExistException("Категория с именем " + newCategoryDto.getName() +
                    " уже существует!");
        }

        Category category = CategoryMapper.toCategory(newCategoryDto);
        category = repository.saveAndFlush(category);
        return CategoryMapper.toCategoryDto(category);
    }

    /**
     * Изменение категории
     *
     * @param categoryDto
     * @return
     */
    @Override
    public CategoryDto patch(CategoryDto categoryDto) {
        if (categoryDto.getId() == null || categoryDto.getName() == null) {
            throw new ValidationException("Некорректный запрос изменения категории!");
        }

        if (repository.existsByName(categoryDto.getName())) {
            throw new EntityAlreadyExistException("Категория с именем " + categoryDto.getName() +
                    " уже существует!");
        }

        Category category = CategoryMapper.toCategory(categoryDto);
        category = repository.saveAndFlush(category);
        return CategoryMapper.toCategoryDto(category);
    }

    /**
     * Удаление категории
     *
     * @param id
     */
    @Override
    public void del(long id) {
        get(id);
        repository.deleteById(id);
    }

    @Override
    public Category get(long id) {
        Optional<Category> category = repository.findById(id);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new EntityNotFoundException("Категория #" + id + " не существует!");
        }
    }
}
