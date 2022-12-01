package ru.practicum.explore.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

/**
 * Информация для редактирования события администратором. Все поля необязательные. Значение полей не валидируется.
 */

@Data
@Builder
@Jacksonized
public class AdminUpdateEventRequestDto {
    private String annotation;          //Краткое описание события
    private Long category;              //id категории к которой относится событие
    private String description;         //Полное описание события
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;    //Дата и время на которые намечено событие
    private Location location;          //Широта и долгота места проведения события
    private Boolean paid;               //Нужно ли оплачивать участие в событии
    private Integer participantLimit;   //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration;  //Нужна ли пре-модерация заявок на участие
    private String title;               //Заголовок события
}
