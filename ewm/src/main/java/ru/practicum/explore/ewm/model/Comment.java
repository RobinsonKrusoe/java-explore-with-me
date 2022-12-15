package ru.practicum.explore.ewm.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * Класс комментария на событие
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                    //Идентификатор записи
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;                //Идентификатор события
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;                //Пользователь, оставивший комментарий (краткая информация)
    private LocalDateTime createdOn;    //Дата и время создания комментария
    private String text;                //Текст коментария
    @Enumerated(EnumType.STRING)
    private CommentState status;        //Статус комментария
    private LocalDateTime changedOn;    //Дата и время последнего изменения комментария (отмены, блокировки, редактирования)
}

