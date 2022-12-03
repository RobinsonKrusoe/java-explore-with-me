package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.explore.ewm.model.EventState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Информация для редактирования события администратором. Все поля необязательные. Значение полей не валидируется.
 */

@Getter
@Setter
@Builder
public class EventFullDto {
    private Long id;                    //Идентификатор
    @NotBlank
    private String annotation;          //Краткое описание
    @NotNull
    private CategoryDto category;       //Категория
    private Integer confirmedRequests;  //Количество одобренных заявок на участие в данном событии
    private String createdOn;           //Дата и время создания события
    private String description;         //Полное описание события
    private String eventDate;           //Дата и время на которые намечено событие
    @NotNull
    private UserShortDto initiator;     //Пользователь (краткая информация)
    @NotNull
    private Location location;          //Широта и долгота места проведения события
    @NotNull
    private Boolean paid;               //Нужно ли оплачивать участие
    private Integer participantLimit;   //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private String publishedOn;         //Дата и время публикации события
    private Boolean requestModeration;  //Нужна ли пре-модерация заявок на участие
    private EventState state;           //Состояние события
    @NotBlank
    private String title;               //Заголовок
    private Integer views;              //Количество просмотрев события
}
