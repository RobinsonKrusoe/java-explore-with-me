package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDto {
    private Long id;        //Идентификатор записи
    private String name;    //Имя категории
}