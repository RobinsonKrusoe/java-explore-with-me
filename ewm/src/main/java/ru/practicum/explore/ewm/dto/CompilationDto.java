package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
public class CompilationDto {
    @NotNull
    private Long id;                        //Идентификатор записи
    private List<EventShortDto> events;     //Список идентификаторов событий входящих в подборку
    @NotNull
    private Boolean pinned;                 //Закреплена ли подборка на главной странице сайта
    @NotBlank
    private String title;                   //Заголовок подборки
}
