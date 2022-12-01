package ru.practicum.explore.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Данные для изменения информации о событии
 */

@Data
@Builder
@Jacksonized
public class UpdateEventRequestDto {
    @Size(max = 2000, min = 20, message = "Максимальная длина аннотации события — 2000 символов, минимальная - 20!")
    private String annotation;          //Новая аннотация
    private Long category;              //Новая категория
    @Size(max = 7000, min = 20, message = "Максимальная длина описания события — 7000 символов, минимальная - 20!")
    private String description;         //Новое описание
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;    //Новые дата и время на которые намечено событие
    private Long eventId;               //Идентификатор события
    private Boolean paid;               //Новое значение флага о платности мероприятия
    private Integer participantLimit;   //Новый лимит пользователей
    @Size(max = 120, min = 3, message = "Максимальная длина заголовка события — 120 символов, минимальная - 3!")
    private String title;               //Новый заголовок
}
