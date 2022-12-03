package ru.practicum.explore.ewm.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс запроса участия в событии
 */
@Entity
@Table(name = "event_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;            //уникальный идентификатор
    private LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Enumerated(EnumType.STRING)
    private RequestState status;
}

