package ru.practicum.explore.stats.service;

import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.dto.ViewDto;

import org.springframework.stereotype.Component;
import ru.practicum.explore.stats.mapper.StatsMapper;
import ru.practicum.explore.stats.model.Hit;
import ru.practicum.explore.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Сервис для работы со статистикой
 */
@Component
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    public StatsServiceImpl(StatsRepository repository) {
        this.repository = repository;
    }

    @Override
    public HitDto add(HitDto hitDto) {
        Hit hit = StatsMapper.toHit(hitDto);
        hit = repository.saveAndFlush(hit);
        return StatsMapper.toHitDto(hit);
    }

    @Override
    public Collection<ViewDto> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<String> uURIs = new ArrayList<>(uris);
        List<ViewDto> ret = new ArrayList<>();

        for (int i = 0; i < uURIs.size(); i++) {
            uURIs.set(i, uURIs.get(i).toUpperCase());
        }

        List<Object[]> result = null;
        if (unique) {
            result = repository.findStatsUnique(start, end, uURIs);
        } else {
            result = repository.findStats(start, end, uURIs);
        }

        if (result != null && !result.isEmpty()) {
            for (Object[] object : result) {
                ret.add(ViewDto.builder()
                            .app(object[0].toString())
                            .uri(object[1].toString())
                            .hits(Integer.valueOf(object[2].toString()))
                            .build());
            }
        }

        return ret;
    }
}
