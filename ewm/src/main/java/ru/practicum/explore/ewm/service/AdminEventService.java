package ru.practicum.explore.ewm.service;

import ru.practicum.explore.ewm.dto.AdminUpdateEventRequestDto;
import ru.practicum.explore.ewm.dto.EventFullDto;
import ru.practicum.explore.ewm.model.Event;

import java.util.Collection;
import java.util.List;

/**
 * API для работы с событиями
 */
public interface AdminEventService {
    /**
     * Поиск событий
     * @param users список id пользователей, чьи события нужно найти
     * @param states список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd дата и время не позже которых должно произойти событие
     * @param from количество событий, которые нужно пропустить для формирования текущего набора
     * @param size количество событий в наборе
     * @return
     */
    Collection<EventFullDto> search(List<Long> users,
                                    List<String> states,
                                    List<Long> categories,
                                    String rangeStart,
                                    String rangeEnd,
                                    Integer from,
                                    Integer size);

    /**
     * Редактирование события
     * @param eventDto
     * @param eventId
     * @return
     */
    EventFullDto save(AdminUpdateEventRequestDto eventDto, Long eventId);

    /**
     * Публикация события
     * @param eventId
     * @return
     */
    EventFullDto publish(Long eventId);

    /**
     * Отклонение события
     * @param eventId
     * @return
     */
    EventFullDto reject(Long eventId);

    /**
     * Для работы с другими сервисами
     * @param eventId
     * @return
     */
    Event get(Long eventId);
}
