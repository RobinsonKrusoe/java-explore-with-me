package ru.practicum.explore.ewm.service;

import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Component
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

        Page<User> retPage = repository.getAllByIdIn(ids, pagingSet);

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
    public UserDto add(NewUserRequestDto newUserDto) {
        if (newUserDto.getEmail() == null || newUserDto.getName() == null) {
            throw new ValidationException("Некорректный запрос на добавление пользователя!");
        }

        if (repository.existsByName(newUserDto.getName())) {
            throw new EntityAlreadyExistException("Пользователь с именем " + newUserDto.getName() +
                    " уже существует!");
        }

        User user = UserMapper.toUser(newUserDto);
        user = repository.saveAndFlush(user);
        return UserMapper.toUserDto(user);
    }

    /**
     * Получение пользователя п идентификатору
     *
     * @param id
     */
    @Override
    public User getUser(long id) {
        Optional<User> user = repository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new EntityNotFoundException("Пользователь #" + id + "не существует!");
        }
    }

    /**
     * Удаление пользователя
     *
     * @param id
     */
    @Override
    public void del(long id) {
        getUser(id);
        repository.deleteById(id);
    }
}
