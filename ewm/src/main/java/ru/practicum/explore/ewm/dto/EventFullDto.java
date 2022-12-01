package ru.practicum.explore.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.explore.ewm.model.EventState;

import java.time.LocalDateTime;

/**
 * Информация для редактирования события администратором. Все поля необязательные. Значение полей не валидируется.
 */

@Data
@Builder
public class EventFullDto {
    private Long id;                    //Идентификатор
    private String annotation;          //Краткое описание
    private CategoryDto category;       //Категория
    private Integer confirmedRequests;  //Количество одобренных заявок на участие в данном событии
    private String createdOn;           //Дата и время создания события
    private String description;         //Полное описание события
    private String eventDate;           //Дата и время на которые намечено событие
    private UserShortDto initiator;     //Пользователь (краткая информация)
    private Location location;          //Широта и долгота места проведения события
    private Boolean paid;               //Нужно ли оплачивать участие
    private Integer participantLimit;   //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private String publishedOn;         //Дата и время публикации события
    private Boolean requestModeration;  //Нужна ли пре-модерация заявок на участие
    private EventState state;           //Состояние события
    private String title;               //Заголовок
    private Integer views;              //Количество просмотрев события
}
