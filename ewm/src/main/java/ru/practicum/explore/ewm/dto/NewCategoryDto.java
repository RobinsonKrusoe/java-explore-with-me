package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCategoryDto {
    private String dummy;   //Пустышка. т.к. когда в классе только одно поле name этот springfuck падает
    private String name;    //Название категории
}
