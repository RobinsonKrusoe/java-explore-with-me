package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import ru.practicum.explore.ewm.dto.EventFullDto;
import ru.practicum.explore.ewm.dto.EventShortDto;
import ru.practicum.explore.ewm.dto.HitDto;
import ru.practicum.explore.ewm.service.PublicEventService;
import ru.practicum.explore.ewm.service.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

/**
 * Публичный API для работы с событиями
 */
@Slf4j
@RestController
@RequestMapping(path = "/events")
public class PublicEventsController {
    private final PublicEventService service;
    private final StatsService statsService;
    private final String app;

    public PublicEventsController(PublicEventService service,
                                  StatsService statsService,
                                  @Value("${app-name-for-stat}") String app) {
        this.service = service;
        this.statsService = statsService;
        this.app = app;
    }

    /**
     * Получение событий с возможностью фильтрации
     *
     * @param text текст для поиска в содержимом аннотации и подробном описании события
     * @param categories список идентификаторов категорий в которых будет вестись поиск
     * @param paid поиск только платных/бесплатных событий
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd дата и время не позже которых должно произойти событие
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     * @param sort Вариант сортировки: по дате события или по количеству просмотров (EVENT_DATE, VIEWS)
     * @param from количество событий, которые нужно пропустить для формирования текущего набора
     * @param size количество событий в наборе
     * @return
     */
    @GetMapping
    public Collection<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false", required = false) Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            HttpServletRequest request) {

        log.info("Get search with text={}, categories={}, paid={}, rangeStart={}, " +
                        "rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);

        statsService.add(HitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());

        return service.search(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size);
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Long id,
                                 HttpServletRequest request) {
        log.info("Get EventFull with id={}", id);
        statsService.add(HitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build());
        return service.get(id);
    }
}
