package ru.practicum.explore.stats.service;

import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.dto.ViewDto;

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
                                  Boolean unique);
}
