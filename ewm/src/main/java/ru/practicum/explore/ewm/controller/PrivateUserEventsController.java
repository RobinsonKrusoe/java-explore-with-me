package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.*;
import ru.practicum.explore.ewm.service.PrivateUserEventService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Закрытый API для работы с событиями
 */
@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
public class PrivateUserEventsController {
    private final PrivateUserEventService service;

    public PrivateUserEventsController(PrivateUserEventService service) {
        this.service = service;
    }

    /**
     * Получение событий, добавленных текущим пользователем
     * @param userId id текущего пользователя
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return
     */
    @GetMapping
    public Collection<EventShortDto> getOwnEvents(@PathVariable Long userId,
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("Get Events with userId={}, from={}, size={}", userId, from, size);

        return service.getOwnEvents(userId, from, size);
    }

    /**
     * Изменение события добавленного текущим пользователем
     * @param userId
     * @param updateEventRequestDto
     * @return
     */
    @PatchMapping
    public EventFullDto patchEvent(@PathVariable Long userId,
                                   @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto) {

        log.info("Patch Event with userId={}, updateEventRequestDto {}", userId, updateEventRequestDto);

        return service.patchEvent(userId, updateEventRequestDto);
    }

    /**
     * Добавление нового события
     * @param userId
     * @param newEventDto
     * @return
     */
    @PostMapping
    public EventFullDto postEvent(@PathVariable Long userId,
                                  @Valid @RequestBody NewEventDto newEventDto) {

        log.info("Post Event with userId={}, newEventDto {}", userId, newEventDto);

        return service.postEvent(userId, newEventDto);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * @param userId
     * @param eventId
     * @return
     */
    @GetMapping("/{eventId}")
    public EventFullDto getOwnEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {

        log.info("Get Event with userId={}, eventId={}", userId, eventId);

        return service.getOwnEvent(userId, eventId);
    }

    /**
     * Отмена события добавленного текущим пользователем
     * @param userId
     * @param eventId
     * @return
     */
    @PatchMapping("/{eventId}")
    public EventFullDto canselOwnEvent(@PathVariable Long userId,
                                       @PathVariable Long eventId) {

        log.info("Cansel Event with userId={}, eventId={}", userId, eventId);

        return service.canselOwnEvent(userId, eventId);
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * @param userId
     * @param eventId
     * @return
     */
    @GetMapping("/{eventId}/requests")
    public Collection<ParticipationRequestDto> getParticipationRequests(@PathVariable Long userId,
                                                                        @PathVariable Long eventId) {

        log.info("Get Participation with userId={}, eventId={}", userId, eventId);

        return service.getParticipationRequests(userId, eventId);
    }

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     * @param userId
     * @param eventId
     * @param reqId
     * @return
     */
    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipationRequest(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @PathVariable Long reqId) {

        log.info("Confirm Participation with userId={}, eventId={}, reqId={}", userId, eventId, reqId);

        return service.confirmParticipationRequest(userId, eventId, reqId);
    }

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     * @param userId
     * @param eventId
     * @param reqId
     * @return
     */
    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipationRequest(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @PathVariable Long reqId) {

        log.info("Reject Participation with userId={}, eventId={}, reqId={}", userId, eventId, reqId);

        return service.rejectParticipationRequest(userId, eventId, reqId);
    }
}
