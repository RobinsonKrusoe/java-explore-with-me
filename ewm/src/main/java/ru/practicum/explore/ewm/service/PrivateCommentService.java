package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.CommentDto;

import java.util.Collection;

/**
 * API для работы с комментариями
 */
public interface PrivateCommentService {
    /**
     * Добавление нового комментария
     * Комментирование доступно всем пользователям для возможности сообщить о накладках мероприятия,
     * непозволивших его посетить
     * @param userId
     * @param comment
     * @return
     */
    CommentDto addComment(Long userId, CommentDto comment);

    /**
     * Изменение комментария
     * Может делать только автор
     * @param userId
     * @param comment
     * @return
     */
    CommentDto changeComment(Long userId, CommentDto comment);

    /**
     * Отмена комментария
     * Если отменяет автор, комментарий переходит в статус CANCELED
     * Если отменяет создатель события, комментарий переходит в статус BLOCKED
     * @param userId
     * @param commentId
     * @return
     */
    CommentDto cancelComment(Long userId, Long commentId);

    /**
     * Получение своих комментариев
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    Collection<CommentDto> getOwnComments(Long userId, Long eventId, Integer from, Integer size);

    /**
     * Получение комментариев по отдельному событию
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    Collection<CommentDto> getEventComments(Long userId, Long eventId, Integer from, Integer size);
}
