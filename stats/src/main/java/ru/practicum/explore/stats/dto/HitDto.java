package ru.practicum.explore.stats.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class HitDto {
    private Long id;    //Идентификатор записи
    private String app; //Идентификатор сервиса для которого записывается информация
    private String uri; //URI для которого был осуществлен запрос
    private String ip;  //IP-адрес пользователя, осуществившего запрос
    private String timestamp;
}
