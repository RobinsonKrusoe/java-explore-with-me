package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Заявка на участие в событии
 */

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;                //Идентификатор заявки
    private String created;         //Дата и время создания заявки
    private Long event;             //Идентификатор события
    private Long requester;         //Идентификатор пользователя, отправившего заявку
    private String status;          //Статус заявки
}
