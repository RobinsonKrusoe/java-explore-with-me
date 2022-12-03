package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class UserShortDto {
    @NotNull
    private Long id;        //Идентификатор записи
    @NotBlank
    private String name;    //Имя категории
}
