package ru.practicum.explore.ewm.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.explore.ewm.dto.CompilationDto;
import ru.practicum.explore.ewm.dto.EventFullDto;
import ru.practicum.explore.ewm.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * API для работы с событиями
 */
public interface PublicCompilationService {
    /**
     * Получение подборок событий
     * @param pinned искать только закрепленные/не закрепленные подборки
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return
     */
    Collection<CompilationDto> search(Boolean pinned, Integer from,Integer size);

    /**
     * Получение подборки событий по его id
     * @param compId
     * @return
     */
    CompilationDto get(Long compId);
}
