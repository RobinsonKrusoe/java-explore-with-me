package ru.practicum.explore.ewm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.errorHandle.exception.EntityAlreadyExistException;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.CategoryDto;
import ru.practicum.explore.ewm.dto.NewCategoryDto;
import ru.practicum.explore.ewm.mapper.CategoryMapper;
import ru.practicum.explore.ewm.model.Category;
import ru.practicum.explore.ewm.repository.CategoryRepository;
import ru.practicum.explore.ewm.repository.EventRepository;

@Service
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    public AdminCategoryServiceImpl(CategoryRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    /**
     * Добавление новой категории
     *
     * @param newCategoryDto
     * @return
     */
    @Override
    @Transactional
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        if (newCategoryDto.getName() == null) {
            throw new ValidationException("Пустое иия категории!");
        }

        if (repository.existsByName(newCategoryDto.getName())) {
            throw new EntityAlreadyExistException("Категория с именем " + newCategoryDto.getName() +
                    " уже существует!");
        }

        Category category = CategoryMapper.toCategory(newCategoryDto);
        repository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    /**
     * Изменение категории
     *
     * @param categoryDto
     * @return
     */
    @Override
    @Transactional
    public CategoryDto patch(CategoryDto categoryDto) {
        if (categoryDto.getId() == null || categoryDto.getName() == null) {
            throw new ValidationException("Некорректный запрос изменения категории!");
        }

        if (repository.existsByName(categoryDto.getName())) {
            throw new EntityAlreadyExistException("Категория с именем " + categoryDto.getName() +
                    " уже существует!");
        }

        Category category = CategoryMapper.toCategory(categoryDto);
        repository.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    /**
     * Удаление категории
     *
     * @param catId
     */
    @Override
    @Transactional
    public void del(long catId) {
        if (!eventRepository.existsByCategory_Id(catId)) {
            repository.deleteById(catId);
        }
    }
}
