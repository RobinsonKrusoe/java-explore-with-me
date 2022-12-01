package ru.practicum.explore.stats.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewDto {
    private String app; //Название сервиса
    private String uri; //URI сервиса
    private int hits;   //Количество просмотров
}
