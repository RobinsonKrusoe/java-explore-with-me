package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.dto.NewCompilationDto;
import ru.practicum.explore.ewm.service.AdminCompilationsService;

import javax.validation.Valid;

/**
 * Контроллер API для работы с подборками событий
 */
@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationsController {
    private final AdminCompilationsService service;

    public AdminCompilationsController(AdminCompilationsService service) {
        this.service = service;
    }

    /**
     * Добавление новой подборки
     * @param newCompilationDto
     * @return
     */
    @PostMapping
    public CompilationDto postCompilation(@Valid  @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post NewCompilation with newCompilationDto={}", newCompilationDto);
        return service.add(newCompilationDto);
    }

    /**
     * Удаление подборки
     * @param compId
     */
    @DeleteMapping("/{compId}")
    public void delCompilation(@PathVariable Long compId) {
        log.info("Delete Compilation with compId={}", compId);
        service.del(compId);
    }

    /**
     * Удалить событие из подборки
     * @param compId
     * @param eventId
     */
    @DeleteMapping("/{compId}/events/{eventId}")
    public void delEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Delete EventFromCompilation with compId={}, compId={}", compId, eventId);
        service.delEvent(compId, eventId);
    }

    /**
     * Добавить событие в подборку
     * @param compId
     * @param eventId
     */
    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        log.info("Add EventToCompilation with compId={}, compId={}", compId, eventId);
        service.addEvent(compId, eventId);
    }

    /**
     * Открепить подборку на главной странице
     * @param compId
     */
    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Unpin Compilation with compId={}", compId);
        service.unpin(compId);
    }

    /**
     * Закрепить подборку на главной странице
     * @param compId
     */
    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Pin Compilation with compId={}", compId);
        service.pin(compId);
    }
}
