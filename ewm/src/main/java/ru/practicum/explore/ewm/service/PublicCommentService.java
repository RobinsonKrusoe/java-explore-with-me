package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.CommentDto;

import java.util.Collection;

/**
 * Публичное API для работы с комментариями
 */
public interface PublicCommentService {

    /**
     * Получение комментариев по отдельному событию
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    Collection<CommentDto> getEventComments(Long eventId, Integer from, Integer size);
}
