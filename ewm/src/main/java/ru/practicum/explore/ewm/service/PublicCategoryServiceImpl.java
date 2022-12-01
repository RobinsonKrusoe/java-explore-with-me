package ru.practicum.explore.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.ewm.dto.CategoryDto;
import ru.practicum.explore.ewm.mapper.CategoryMapper;
import ru.practicum.explore.ewm.model.Category;
import ru.practicum.explore.ewm.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository repository;

    public PublicCategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Получение категорий
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора
     * @param size количество категорий в наборе
     * @return
     */
    @Override
    public Collection<CategoryDto> get(Integer from, Integer size) {
        Pageable pagingSet = PageRequest.of(from / size, size);

        Page<Category> retPage = repository.findAll(pagingSet);

        List<CategoryDto> ret = new ArrayList<>();
        for (Category category : retPage) {
            ret.add(CategoryMapper.toCategoryDto(category));
        }

        return ret;
    }

    /**
     * Получение информации о категории по её идентификатору
     *
     * @param catId
     * @return
     */
    @Override
    public CategoryDto get(Long catId) {
        Optional<Category> category = repository.findById(catId);
        if (category.isPresent()) {
            return CategoryMapper.toCategoryDto(category.get());
        } else {
            throw new EntityNotFoundException("Категория #" + catId + " не существует!");
        }

    }
}
