package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.ParticipationRequestDto;

import java.util.Collection;

/**
 * Закрытый API для работы с запросами текущего пользователя на участие в событиях
 */
public interface PrivateUserRequestService {
    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     * @param userId
     * @return
     */
    Collection<ParticipationRequestDto> get(Long userId);

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     * @param userId id текущего пользователя
     * @param eventId
     * @return
     */
    ParticipationRequestDto addRequestToEvent(Long userId, Long eventId);

    /**
     * Отмена своего запроса на участие в событии
     * @param userId
     * @param requestId
     * @return
     */
    ParticipationRequestDto canselOwnRequest(Long userId, Long requestId);
}
