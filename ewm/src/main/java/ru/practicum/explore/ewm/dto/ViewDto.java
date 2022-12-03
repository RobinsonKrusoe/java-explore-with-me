package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ViewDto {
    private String app;     //Название сервиса
    private String uri;     //URI сервиса
    private Integer hits;   //Количество просмотров
}
