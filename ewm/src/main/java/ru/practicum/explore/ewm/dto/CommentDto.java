package ru.practicum.explore.ewm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import ru.practicum.explore.ewm.model.CommentState;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Комментарий на событие
 */
@Getter
@Setter
@Builder
public class CommentDto {
    private Long id;                //Идентификатор записи
    @NotNull
    private Long event;             //Идентификатор события
    private UserShortDto author;    //Пользователь, оставивший комментарий (краткая информация)
    private String createdOn;       //Дата и время создания комментария
    @NotBlank
    @Size(max = 2000, min = 20, message = "Максимальная длина комментария — 2000 символов, минимальная - 20!")
    private String text;            //Текст коментария
    private CommentState status;    //Статус комментария
    private String changedOn;       //Дата и время последнего изменения комментария (отмены, блокировки, редактирования)
}
