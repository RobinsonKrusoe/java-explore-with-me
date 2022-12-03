package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class EventShortDto {
    private Long id;                    //Идентификатор
    @NotBlank
    private String annotation;          //Краткое описание
    @NotNull
    private CategoryDto category;       //Категория
    private Integer confirmedRequests;  //Количество одобренных заявок на участие в данном событии
    private String eventDate;           //Дата и время на которые намечено событие
    @NotNull
    private UserShortDto initiator;     //Пользователь (краткая информация)
    @NotNull
    private Boolean paid;               //Нужно ли оплачивать участие
    @NotBlank
    private String title;               //Заголовок
    private Integer views;              //Количество просмотрев события
}
