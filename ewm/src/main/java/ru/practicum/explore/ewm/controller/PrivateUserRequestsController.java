package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.service.PrivateUserRequestService;

import java.util.Collection;

/**
 * Закрытый API для работы с запросами текущего пользователя на участие в событиях
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateUserRequestsController {
    private final PrivateUserRequestService service;

    public PrivateUserRequestsController(PrivateUserRequestService service) {
        this.service = service;
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * @param userId
     * @return
     */
    @GetMapping
    public Collection<ParticipationRequestDto> get(@PathVariable Long userId) {
        log.info("Get ParticipationRequests with userId={}", userId);
        return service.get(userId);
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * @param userId id текущего пользователя
     * @param eventId
     * @return
     */
    @PostMapping
    public ParticipationRequestDto addRequestToEvent(@PathVariable Long userId,
                                                     @RequestParam Long eventId) {
        log.info("Request for Event with userId={}, eventId={}", userId, eventId);
        return service.addRequestToEvent(userId, eventId);
    }

    /**
     * Отмена своего запроса на участие в событии
     * @param userId
     * @param requestId
     * @return
     */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto canselOwnRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        log.info("Cansel own Request for Event with userId={}, requestId={}", userId, requestId);
        return service.canselOwnRequest(userId, requestId);
    }
}
