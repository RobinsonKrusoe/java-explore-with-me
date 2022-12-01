package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.AdminUpdateEventRequestDto;
import ru.practicum.explore.ewm.dto.EventFullDto;
import ru.practicum.explore.ewm.service.AdminEventService;

import java.util.Collection;
import java.util.List;

/**
 * Контроллер API для работы с событиями
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventsController {
    private final AdminEventService service;

    public AdminEventsController(AdminEventService service) {
        this.service = service;
    }

    /**
     * Поиск событий
     * @param users список id пользователей, чьи события нужно найти
     * @param states список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd дата и время не позже которых должно произойти событие
     * @param from количество событий, которые нужно пропустить для формирования текущего набора
     * @param size количество событий в наборе
     * @return
     */
    @GetMapping
    public Collection<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0",required = false) Integer from,
            @RequestParam(defaultValue = "10",required = false) Integer size) {

        log.info("Get Events with users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return service.search(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    /**
     * Редактирование события
     * @param eventDto
     * @param eventId
     * @return
     */

    @PutMapping("/{eventId}")
    public EventFullDto saveEvent(@RequestBody AdminUpdateEventRequestDto eventDto,
                                  @PathVariable Long eventId) {
        log.info("Save Event with eventDto {}, eventId={}", eventDto, eventId);
        return service.save(eventDto, eventId);
    }

    /**
     * Публикация события
     * @param eventId
     * @return
     */
    @PatchMapping("{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Publish Event with eventId={}", eventId);
        return service.publish(eventId);
    }

    /**
     * Отклонение события
     * @param eventId
     * @return
     */
    @PatchMapping("{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        log.info("Reject Event with eventId={}", eventId);
        return service.reject(eventId);
    }
}
