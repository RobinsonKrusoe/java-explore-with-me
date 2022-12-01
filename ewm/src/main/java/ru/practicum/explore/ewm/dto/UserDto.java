package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.Email;

@Data
@Builder
public class UserDto {
    private Long id;        //Идентификатор записи
    private String name;    //Имя категории
    @Email(message = "Некорректный Email!")
    private String email;   //Электронная почта
}
