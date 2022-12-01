package ru.practicum.explore.ewm.dto;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;

/**
 * Новое событие
 */
@Data
@Builder
@Jacksonized
public class NewEventDto {
    @NotNull
    @Size(max = 2000, min = 20, message = "Максимальная длина аннотации события — 2000 символов, минимальная - 20!")
    private String annotation;          //Краткое описание события
    @NotNull
    private Long category;              //id категории к которой относится событие
    @Size(max = 7000, min = 20, message = "Максимальная длина описания события — 7000 символов, минимальная - 20!")
    @NotNull
    private String description;         //Полное описание события
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;    //Дата и время на которые намечено событие
    @NotNull
    private Location location;          //Широта и долгота места проведения события
    private Boolean paid;               //Нужно ли оплачивать участие в событии
    private Integer participantLimit;   //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Boolean requestModeration;  //Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут
                                        //ожидать подтверждения инициатором события. Если false - то будут
                                        //подтверждаться автоматически.
    @NotNull
    @Size(max = 120, min = 3, message = "Максимальная длина заголовка события — 120 символов, минимальная - 3!")
    private String title;               //Заголовок события
}
