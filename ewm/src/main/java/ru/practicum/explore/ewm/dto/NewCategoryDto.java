package ru.practicum.explore.ewm.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NewCategoryDto {
    @NotBlank
    private String name;    //Название категории
}
