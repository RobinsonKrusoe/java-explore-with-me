package ru.practicum.explore.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.errorHandle.exception.EntityAlreadyExistException;
import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.NewUserRequestDto;
import ru.practicum.explore.ewm.dto.UserDto;
import ru.practicum.explore.ewm.mapper.UserMapper;
import ru.practicum.explore.ewm.model.User;
import ru.practicum.explore.ewm.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository repository;

    public AdminUserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Получение пользователей по идентификаторам
     *
     * @param ids
     * @param from
     * @param size
     */
    @Override
    public Collection<UserDto> get(List<Long> ids, Integer from, Integer size) {
        Pageable pagingSet = PageRequest.of(from / size, size);
        Page<User> retPage = null;

        if (ids != null && ids.size() > 0) {
            retPage = repository.findAllByIdIn(ids, pagingSet);
        } else {
            retPage = repository.findAll(pagingSet);
        }

        List<UserDto> ret = new ArrayList<>();
        for (User user : retPage) {
            ret.add(UserMapper.toUserDto(user));
        }

        return ret;
    }

    /**
     * Добавление нового пользователя
     *
     * @param newUserDto
     */
    @Override
    @Transactional
    public UserDto add(NewUserRequestDto newUserDto) {
        if (newUserDto.getEmail() == null || newUserDto.getName() == null) {
            throw new ValidationException("Некорректный запрос на добавление пользователя!");
        }

        if (repository.existsByName(newUserDto.getName())) {
            throw new EntityAlreadyExistException("Пользователь с именем " + newUserDto.getName() +
                    " уже существует!");
        }

        User user = UserMapper.toUser(newUserDto);
        repository.save(user);
        return UserMapper.toUserDto(user);
    }

    /**
     * Получение пользователя п идентификатору
     *
     * @param userId
     */
    private User getUser(Long userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь #" + userId + " не существует!"));
    }

    /**
     * Удаление пользователя
     *
     * @param id
     */
    @Override
    @Transactional
    public void del(Long id) {
        getUser(id);
        repository.deleteById(id);
    }
}
