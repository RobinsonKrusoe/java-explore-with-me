package ru.practicum.explore.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    //Идентификатор записи
    private String app; //Идентификатор сервиса для которого записывается информация
    private String uri; //URI для которого был осуществлен запрос
    private String ip;  //IP-адрес пользователя, осуществившего запрос
    @Column(name = "time_stamp")
    private LocalDateTime timestamp;
}
