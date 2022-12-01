package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.dto.NewCompilationDto;

/**
 * Интерфейс для работы с подборками событий
 *
 */
public interface AdminCompilationsService {
    /**
     * Добавление новой подборки
     * @param newCompilationDto
     * @return
     */
    CompilationDto add(NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки
     * @param compId
     */
    void del(Long compId);

    /**
     * Удалить событие из подборки
     * @param compId
     * @param eventId
     */
    void delEvent(Long compId, Long eventId);

    /**
     * Добавить событие в подборку
     * @param compId
     * @param eventId
     */
    void addEvent(Long compId, Long eventId);

    /**
     * Открепить подборку на главной странице
     * @param compId
     */
    void unpin(Long compId);

    /**
     * Закрепить подборку на главной странице
     * @param compId
     */
    void pin(Long compId);
}
