package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.*;

import java.util.Collection;

/**
 * Закрытый API для работы с запросами текущего пользователя на участие в событиях
 */
public interface PrivateUserEventService {
    /**
     * Получение событий, добавленных текущим пользователем
     * @param userId id текущего пользователя
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size количество элементов в наборе
     * @return
     */
    Collection<EventShortDto> getOwnEvents(Long userId, Integer from, Integer size);

    /**
     * Изменение события добавленного текущим пользователем
     * @param userId
     * @param updateEventRequestDto
     * @return
     */
    EventFullDto patchEvent(Long userId, UpdateEventRequestDto updateEventRequestDto);

    /**
     * Добавление нового события
     * @param userId
     * @param newEventDto
     * @return
     */
    EventFullDto postEvent(Long userId, NewEventDto newEventDto);

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     * @param userId
     * @param eventId
     * @return
     */
    EventFullDto getOwnEvent(Long userId, Long eventId);

    /**
     * Отмена события добавленного текущим пользователем
     * @param userId
     * @param eventId
     * @return
     */
    EventFullDto canselOwnEvent(Long userId, Long eventId);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     * @param userId
     * @param eventId
     * @return
     */
    Collection<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId);

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     * @param userId
     * @param eventId
     * @param reqId
     * @return
     */
    ParticipationRequestDto confirmParticipationRequest(Long userId, Long eventId, Long reqId);

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     * @param userId
     * @param eventId
     * @param reqId
     * @return
     */
    ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long reqId);
}
