package ru.practicum.explore.ewm.mapper;

import ru.practicum.explore.ewm.dto.CategoryDto;
import ru.practicum.explore.ewm.dto.NewCategoryDto;
import ru.practicum.explore.ewm.model.Category;

public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        if (category != null) {
            return CategoryDto.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build();
        } else {
            return null;
        }
    }

    public static Category toCategory(NewCategoryDto categoryDto) {
        if (categoryDto != null) {
            Category category = new Category();
            category.setName(categoryDto.getName());
            return category;
        } else {
            return null;
        }
    }

    public static Category toCategory(CategoryDto categoryDto) {
        if (categoryDto != null) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setName(categoryDto.getName());
            return category;
        } else {
            return null;
        }
    }
}
