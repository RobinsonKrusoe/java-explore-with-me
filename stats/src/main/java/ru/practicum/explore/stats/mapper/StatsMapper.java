package ru.practicum.explore.stats.mapper;

import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatsMapper {
    public static HitDto toHitDto(Hit hit){
        if(hit != null){
            HitDto ret = HitDto.builder()
                    .id(hit.getId())
                    .app(hit.getApp())
                    .uri(hit.getUri())
                    .ip(hit.getIp())
                    .timestamp(hit.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .build();

            return ret;

        } else {
            return null;
        }
    }

    public static Hit toHit(HitDto hitDto){
        Hit hit = new Hit();

        if(hitDto.getId() != null){
            hit.setId(hitDto.getId());
        }

        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(),
                         DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return hit;
    }
}
