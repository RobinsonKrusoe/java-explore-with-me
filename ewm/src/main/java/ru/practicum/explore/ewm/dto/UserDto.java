package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;        //Идентификатор записи
    @NotBlank
    private String name;    //Имя категории
    @NotBlank
    @Email(message = "Некорректный Email!")
    private String email;   //Электронная почта
}
