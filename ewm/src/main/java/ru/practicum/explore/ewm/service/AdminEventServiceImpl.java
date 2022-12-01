package ru.practicum.explore.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.ewm.dto.AdminUpdateEventRequestDto;
import ru.practicum.explore.ewm.dto.EventFullDto;
import ru.practicum.explore.ewm.mapper.EventMapper;
import ru.practicum.explore.ewm.model.Category;
import ru.practicum.explore.ewm.model.Event;
import ru.practicum.explore.ewm.model.EventState;
import ru.practicum.explore.ewm.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository repository;
    private final AdminCategoryService categoryService;
    private final StatsService statsService;

    public AdminEventServiceImpl(EventRepository repository,
                                 AdminCategoryService categoryService,
                                 StatsService statsService) {
        this.repository = repository;
        this.categoryService = categoryService;
        this.statsService = statsService;
    }

    /**
     * Поиск событий
     *
     * @param users      список id пользователей, чьи события нужно найти
     * @param states     список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не раньше которых должно произойти событие
     * @param rangeEnd   дата и время не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора
     * @param size       количество событий в наборе
     * @return
     */
    @Override
    public Collection<EventFullDto> search(List<Long> users,
                                            List<String> states,
                                            List<Long> categories,
                                            String rangeStart,
                                            String rangeEnd,
                                            Integer from,
                                            Integer size) {

        Pageable pagingSet = PageRequest.of(from / size, size);

        Specification<Event> spec = null;

        if (users != null && !users.isEmpty()) {
            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.in(root.get("initiator").get("id")).value(users)
            );
        }

        if (states != null && !states.isEmpty()) {
            List<EventState> stList = new ArrayList<>();

            for (String strState : states) {
                stList.add(EventState.valueOf(strState));
            }

            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.in(root.get("state")).value(stList)
            );
        }

        if (rangeStart != null) {
            LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), start)
            );
        }

        if (rangeEnd != null) {
            LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), end)
            );
        }

        Page<Event> retPage = repository.findAll(spec, pagingSet);

        List<EventFullDto> ret = new ArrayList<>();
        for (Event event : retPage) {
            ret.add(EventMapper.toEventFullDto(statsService.fillViews(event)));
        }

        return ret;
    }

    /**
     * Редактирование события
     *
     * @param eventDto
     * @param eventId
     * @return
     */
    @Override
    public EventFullDto save(AdminUpdateEventRequestDto eventDto, Long eventId) {
        Event eventDB = get(eventId);
        if (eventDto.getAnnotation() != null) {
            eventDB.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != null) {
            Category cat = categoryService.get(eventDto.getCategory());
            eventDB.setCategory(cat);
        }

        if (eventDto.getDescription() != null) {
            eventDB.setDescription(eventDto.getDescription());
        }

        if (eventDto.getEventDate() != null) {
            eventDB.setEventDate(eventDto.getEventDate());
        }

        if (eventDto.getLocation() != null) {
            eventDB.setLat(eventDto.getLocation().getLat());
            eventDB.setLon(eventDto.getLocation().getLon());
        }

        if (eventDto.getPaid() != null) {
            eventDB.setPaid(eventDto.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            eventDB.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getRequestModeration() != null) {
            eventDB.setRequestModeration(eventDto.getRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            eventDB.setTitle(eventDto.getTitle());
        }

        eventDB = repository.saveAndFlush(eventDB);

        return EventMapper.toEventFullDto(statsService.fillViews(eventDB));
    }

    /**
     * Публикация события
     *
     * @param eventId
     * @return
     */
    @Override
    public EventFullDto publish(Long eventId) {
        Event eventDB = get(eventId);
        eventDB.setState(EventState.PUBLISHED);
        eventDB.setPublishedOn(LocalDateTime.now());

        eventDB = repository.saveAndFlush(eventDB);

        return EventMapper.toEventFullDto(statsService.fillViews(eventDB));
    }

    /**
     * Отклонение события
     *
     * @param eventId
     * @return
     */
    @Override
    public EventFullDto reject(Long eventId) {
        Event eventDB = get(eventId);
        eventDB.setState(EventState.CANCELED);
        eventDB = repository.saveAndFlush(eventDB);
        return EventMapper.toEventFullDto(statsService.fillViews(eventDB));
    }

    /**
     * Для работы с другими сервисами
     *
     * @param eventId
     * @return
     */
    @Override
    public Event get(Long eventId) {
        Optional<Event> event = repository.findById(eventId);
        if (event.isPresent()) {
            return statsService.fillViews(event.get());
        } else {
            throw new EntityNotFoundException("Событие #" + eventId + " не существует!");
        }
    }
}
