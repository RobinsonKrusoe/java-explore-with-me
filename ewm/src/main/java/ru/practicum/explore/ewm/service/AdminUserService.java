package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.NewUserRequestDto;
import ru.practicum.explore.ewm.dto.UserDto;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для работы с пользователями
 */
public interface AdminUserService {
    /**
     * Получение пользователей по идентификаторам
     */
    Collection<UserDto> get(List<Long> ids, Integer from, Integer size);

    /**
     * Добавление нового пользователя
     */
    UserDto add(NewUserRequestDto newUser);

    /**
     * Удаление пользователя
     */
    void del(Long id);
}
