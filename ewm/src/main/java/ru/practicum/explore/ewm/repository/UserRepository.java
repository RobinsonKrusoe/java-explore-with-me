package ru.practicum.explore.ewm.repository;

import ru.practicum.explore.ewm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> getAllByIdIn(List<Long> ids, Pageable pagingSet);
    boolean existsByName(String name);
}