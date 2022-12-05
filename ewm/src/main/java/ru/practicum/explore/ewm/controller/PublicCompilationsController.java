package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.service.PublicCompilationService;

import java.util.Collection;

/**
 * Публичный API для работы с подборками событий
 */
@Slf4j
@RestController
@RequestMapping(path = "/compilations")
public class PublicCompilationsController {
    private final PublicCompilationService service;

    public PublicCompilationsController(PublicCompilationService service) {
        this.service = service;
    }

    /**
     * Получение подборок событий
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return
     */
    @GetMapping
    public Collection<CompilationDto> search(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        log.info("Get Compilations with pinned={}, from={}, size={}", pinned, from, size);
        return service.search(pinned, from, size);
    }

    /**
     * Получение подборки событий по его id
     * @param compId
     * @return
     */
    @GetMapping("/{compId}")
    public CompilationDto get(@PathVariable Long compId) {
        log.info("Get Compilation with compId={}", compId);
        return service.get(compId);
    }
}
