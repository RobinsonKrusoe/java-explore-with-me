package ru.practicum.explore.ewm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ru.practicum.explore.ewm.dto.HitDto;
import ru.practicum.explore.ewm.dto.ViewDto;
import ru.practicum.explore.ewm.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Интерфейс для работы со статистикой
 */
public interface StatsService {
    HitDto add(HitDto hitDto);

    Collection<ViewDto> findStats(LocalDateTime start,
                                  LocalDateTime end,
                                  List<String> uris,
                                  Boolean unique) throws JsonProcessingException;

    Event fillViews(Event event);
}
