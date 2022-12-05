package ru.practicum.explore.stats.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.dto.ViewDto;
import ru.practicum.explore.stats.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

/**
 * Контроллер API для работы со статистикой посещений
 */
@Slf4j
@RestController
public class StatsController {
    private final StatsService service;

    @Autowired
    public StatsController(StatsService service) {
        this.service = service;
    }

    /**
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем.
     * Название сервиса, uri и ip пользователя указаны в теле запроса.
     */
    @PostMapping("/hit")
    public HitDto postHit(@Valid @RequestBody HitDto hitDto) {
        log.info("Post hit with hitDto {}", hitDto);
        return service.add(hitDto);
    }

    /**
     * Получение статистики по посещениям.
     */
    @GetMapping("/stats")
    public Collection<ViewDto> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique) {

        log.info("Get stats with start={}, end={}, uris={}, unique={}", start, end, uris, unique);

        return service.findStats(
                LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                uris,
                unique);
    }
}
