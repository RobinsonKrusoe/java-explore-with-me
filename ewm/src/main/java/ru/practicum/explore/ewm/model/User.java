package ru.practicum.explore.ewm.model;

import lombok.*;

import javax.persistence.*;

/**
 * Класс пользователя
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;        //уникальный идентификатор пользователя
    private String name;    //имя или логин пользователя
    private String email;   //адрес электронной почты
                            //(два пользователя не могут иметь одинаковый адрес электронной почты).
}
