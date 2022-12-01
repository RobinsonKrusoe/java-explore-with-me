package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class NewUserRequestDto {
    @Email(message = "Некорректный Email!")
    private String email;   //Электронная почта
    private String name;    //Название категории
}
