package ru.practicum.explore.ewm.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.explore.ewm.dto.AdminUpdateEventRequestDto;
import ru.practicum.explore.ewm.dto.EventFullDto;
import ru.practicum.explore.ewm.dto.EventShortDto;
import ru.practicum.explore.ewm.model.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * API для работы с событиями
 */
public interface PublicEventService {
    /**
     * Получение событий с возможностью фильтрации
     *
     * @param text текст для поиска в содержимом аннотации и подробном описании события
     * @param categories список идентификаторов категорий в которых будет вестись поиск
     * @param paid поиск только платных/бесплатных событий
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd дата и время не позже которых должно произойти событие
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     * @param sort Вариант сортировки: по дате события или по количеству просмотров (EVENT_DATE, VIEWS)
     * @param from количество событий, которые нужно пропустить для формирования текущего набора
     * @param size количество событий в наборе
     * @return
     */
    Collection <EventShortDto> search(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size);

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     * @param id
     * @return
     */
    EventFullDto get(Long id);
}
