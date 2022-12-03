package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
public class NewCompilationDto {
    private List<Long> events;  //Список идентификаторов событий входящих в подборку
    private Boolean pinned;     //Закреплена ли подборка на главной странице сайта
    @NotBlank
    private String title;       //Заголовок подборки
}
