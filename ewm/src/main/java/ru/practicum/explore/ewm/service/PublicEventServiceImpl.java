package ru.practicum.explore.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.EventFullDto;
import ru.practicum.explore.ewm.dto.EventShortDto;
import ru.practicum.explore.ewm.mapper.EventMapper;
import ru.practicum.explore.ewm.model.Event;
import ru.practicum.explore.ewm.model.EventRequest;
import ru.practicum.explore.ewm.model.EventState;
import ru.practicum.explore.ewm.model.SortVariant;
import ru.practicum.explore.ewm.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository repository;
    private final StatsService statsService;

    public PublicEventServiceImpl(EventRepository repository, StatsService statsService) {
        this.repository = repository;
        this.statsService = statsService;
    }

    /**
     * Получение событий с возможностью фильтрации
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время не раньше которых должно произойти событие
     * @param rangeEnd      дата и время не позже которых должно произойти событие
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров (EVENT_DATE, VIEWS)
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора
     * @param size          количество событий в наборе
     * @return
     */
    @Override
    public Collection<EventShortDto> search(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            String sort,
            Integer from,
            Integer size) {

        Pageable pagingSet = null;

        if (sort != null) {
            SortVariant sortVariant = null;
            try {
                sortVariant = SortVariant.valueOf(sort);
            } catch (Exception e) {
                throw new ValidationException("Некорректный тип сортировки: " + sort);
            }

            switch (sortVariant) {
                case VIEWS:
                    pagingSet = PageRequest.of(from / size, size, Sort.by("views"));
                    break;
                case EVENT_DATE:
                    pagingSet = PageRequest.of(from / size, size, Sort.by("eventDate"));
                    break;
                default:
                    throw new ValidationException("Сортировка по " + sort + "не поддерживается!");
            }
        } else {
            pagingSet = PageRequest.of(from / size, size);
        }

        Specification<Event> spec = null;

        if (text != null && !text.isEmpty()) {
            Specification<Event> specOr = Specification.where(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.like(root.get("annotation"), "%" + text + "%")
            );

            specOr = Specification.where(specOr).or(
                            (root, criteriaQuery, criteriaBuilder) ->
                                    criteriaBuilder.like(root.get("description"), "%" + text + "%")
                    );

            spec = Specification.where(spec).and(specOr);
        }

        if (categories != null && categories.size() > 0) {
            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.in(root.get("category").get("id")).value(categories)
            );
        }

        if (paid != null) {
            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("paid"), paid)
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

        if (rangeStart == null && rangeEnd == null) {
            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.greaterThan(root.get("eventDate").as(LocalDateTime.class),
                                    LocalDateTime.now())
            );
        }
        if (onlyAvailable != null) {
            Specification<Event> specAva = Specification.where(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("participantLimit"), 0)
            );

            specAva = Specification.where(specAva)
                    .or(
                            (root, criteriaQuery, criteriaBuilder) -> {
                                Subquery<Long> subQ = criteriaQuery.subquery(Long.class);
                                Root<EventRequest> subRoot = subQ.from(EventRequest.class);
                                subQ.select(criteriaBuilder.count(subRoot.get("id")));
                                subQ.where(criteriaBuilder.equal(root.get("id"), subRoot.get("event").get("id")));

                                return criteriaBuilder.lessThan(subQ, root.get("participantLimit"));
                            }
                    );

            spec = Specification.where(spec).and(specAva);
        }

        spec = Specification.where(spec).and(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED)
        );

        Page<Event> retPage = repository.findAll(spec, pagingSet);

        List<EventShortDto> ret = new ArrayList<>();
        for (Event event : retPage) {
            ret.add(EventMapper.toEventShortDto(statsService.fillViews(event)));
        }

        return ret;
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     *
     * @param id
     * @return
     */
    @Override
    public EventFullDto get(Long id) {
        return EventMapper.toEventFullDto(statsService.fillViews(repository.getReferenceById(id)));
    }
}
