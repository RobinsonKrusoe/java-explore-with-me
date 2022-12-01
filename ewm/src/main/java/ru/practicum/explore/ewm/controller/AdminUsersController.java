package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.NewUserRequestDto;
import ru.practicum.explore.ewm.dto.UserDto;
import ru.practicum.explore.ewm.service.AdminUserService;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер API для работы с пользователями
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/users")
public class AdminUsersController {
    private final AdminUserService service;

    public AdminUsersController(AdminUserService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<UserDto> getUsers(@RequestParam List<Long> ids,
                                        @RequestParam(defaultValue = "0",required = false) Integer from,
                                        @RequestParam(defaultValue = "10",required = false) Integer size) {
        log.info("Get Users with ids={}, from={}, size={}", ids, from, size);
        return service.get(ids, from, size);
    }

    @PostMapping
    public UserDto postUser(@RequestBody NewUserRequestDto newUser) {
        log.info("Post User with newUser={}", newUser);
        return service.add(newUser);
    }

    @DeleteMapping("/{userId}")
    public void delUser(@PathVariable Long userId) {
        log.info("Post User with userId={}", userId);
        service.del(userId);
    }
}
