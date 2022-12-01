package ru.practicum.explore.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.explore.ewm.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}