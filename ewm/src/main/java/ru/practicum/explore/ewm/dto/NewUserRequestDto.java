package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class NewUserRequestDto {
    @NotBlank
    @Email(message = "Некорректный Email!")
    private String email;   //Электронная почта
    @NotBlank
    private String name;    //Название категории
}
