package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventShortDto {
    private String annotation;          //Краткое описание
    private CategoryDto category;       //Категория
    private Integer confirmedRequests;  //Количество одобренных заявок на участие в данном событии
    private String eventDate;    //Дата и время на которые намечено событие
    private Long id;                    //Идентификатор
    private UserShortDto initiator;     //Пользователь (краткая информация)
    private Boolean paid;               //Нужно ли оплачивать участие
    private String title;               //Заголовок
    private Integer views;              //Количество просмотрев события
}
