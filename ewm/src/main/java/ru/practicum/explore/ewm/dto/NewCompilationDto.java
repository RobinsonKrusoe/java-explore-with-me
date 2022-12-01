package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    private List<Long> events;  //Список идентификаторов событий входящих в подборку
    private Boolean pinned;     //Закреплена ли подборка на главной странице сайта
    private String title;       //Заголовок подборки
}