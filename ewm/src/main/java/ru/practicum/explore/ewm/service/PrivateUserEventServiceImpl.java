package ru.practicum.explore.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.*;
import ru.practicum.explore.ewm.mapper.EventMapper;
import ru.practicum.explore.ewm.mapper.EventRequestMapper;
import ru.practicum.explore.ewm.model.*;
import ru.practicum.explore.ewm.repository.EventRepository;
import ru.practicum.explore.ewm.repository.EventRequestRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PrivateUserEventServiceImpl implements PrivateUserEventService {
    private final EventRepository repository;
    private final EventRequestRepository requestRepository;
    private final AdminUserService userService;
    private final AdminCategoryService categoryService;
    private final StatsService statsService;

    public PrivateUserEventServiceImpl(EventRepository repository,
                                       EventRequestRepository requestRepository,
                                       AdminUserService userService,
                                       AdminCategoryService categoryService,
                                       StatsService statsService) {
        this.repository = repository;
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.statsService = statsService;
    }

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId id текущего пользователя
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     * @param size   количество элементов в наборе
     * @return
     */
    @Override
    public Collection<EventShortDto> getOwnEvents(Long userId, Integer from, Integer size) {
        User user = userService.getUser(userId);

        Pageable pagingSet = PageRequest.of(from / size, size);

        Page<Event> retPage = repository.getAllByInitiator_Id(userId, pagingSet);

        List<EventShortDto> ret = new ArrayList<>();
        for (Event event : retPage) {
            ret.add(EventMapper.toEventShortDto(statsService.fillViews(event)));
        }

        return ret;
    }

    /**
     * Изменение события добавленного текущим пользователем
     *
     * @param userId
     * @param updateEventRequestDto
     * @return
     */
    @Override
    public EventFullDto patchEvent(Long userId, UpdateEventRequestDto updateEventRequestDto) {
        User user = userService.getUser(userId);

        if (updateEventRequestDto.getEventId() == null) {
            throw new ValidationException("Не задан идентификатор события для изменений!");
        }

        Event eventDB = repository.getReferenceById(updateEventRequestDto.getEventId());

        if (updateEventRequestDto.getAnnotation() != null) {
            eventDB.setAnnotation(updateEventRequestDto.getAnnotation());
        }

        if (updateEventRequestDto.getCategory() != null) {
            Category category = categoryService.get(updateEventRequestDto.getCategory());
            eventDB.setCategory(category);
        }

        if (updateEventRequestDto.getDescription() != null) {
            eventDB.setDescription(updateEventRequestDto.getDescription());
        }

        if (updateEventRequestDto.getEventDate() != null) {
            eventDB.setEventDate(updateEventRequestDto.getEventDate());
        }

        if (updateEventRequestDto.getPaid() != null) {
            eventDB.setPaid(updateEventRequestDto.getPaid());
        }

        if (updateEventRequestDto.getParticipantLimit() != null) {
            eventDB.setParticipantLimit(updateEventRequestDto.getParticipantLimit());
        }

        if (updateEventRequestDto.getTitle() != null) {
            eventDB.setTitle(updateEventRequestDto.getTitle());
        }

        eventDB = repository.saveAndFlush(eventDB);

        return EventMapper.toEventFullDto(statsService.fillViews(eventDB));
    }

    /**
     * Добавление нового события
     *
     * @param userId
     * @param newEventDto
     * @return
     */
    @Override
    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getAnnotation() == null ||
            newEventDto.getCategory() == null ||
            newEventDto.getDescription() == null ||
            newEventDto.getEventDate() == null ||
            newEventDto.getLocation() == null ||
            newEventDto.getTitle() == null) {
            throw new ValidationException("Некорректное событие");
        }

        User initiator = userService.getUser(userId);
        Category category = categoryService.get(newEventDto.getCategory());

        Event newEvent = EventMapper.toEvent(newEventDto, initiator, category);

        newEvent = repository.saveAndFlush(newEvent);

        return EventMapper.toEventFullDto(newEvent);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     *
     * @param userId
     * @param eventId
     * @return
     */
    @Override
    public EventFullDto getOwnEvent(Long userId, Long eventId) {
        User user = userService.getUser(userId);

        Event event = repository.findAllByInitiator_IdAndId(userId, eventId);

        return EventMapper.toEventFullDto(statsService.fillViews(event));
    }

    /**
     * Отмена события добавленного текущим пользователем
     *
     * @param userId
     * @param eventId
     * @return
     */
    @Override
    public EventFullDto canselOwnEvent(Long userId, Long eventId) {
        User user = userService.getUser(userId);

        Event eventDB = repository.getReferenceById(eventId);

        if (eventDB.getInitiator() != null && eventDB.getInitiator().getId() != userId) {
            throw new ValidationException("Можно отменять только свои события!");
        }

        eventDB.setState(EventState.CANCELED);

        return EventMapper.toEventFullDto(statsService.fillViews(eventDB));
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId
     * @param eventId
     * @return
     */
    @Override
    public Collection<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = repository.getReferenceById(eventId);
        List<EventRequest> eventRequests = requestRepository.findAllByEvent_Initiator_IdAndEvent_Id(userId, eventId);

        List<ParticipationRequestDto> ret = new ArrayList<>();
        for (EventRequest eReq : eventRequests) {
            ret.add(EventRequestMapper.toParticipationRequestDto(eReq));
        }

        return ret;
    }

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId
     * @param eventId
     * @param reqId
     * @return
     */
    @Override
    public ParticipationRequestDto confirmParticipationRequest(Long userId, Long eventId, Long reqId) {
        User user = userService.getUser(userId);
        Event event = repository.getReferenceById(eventId);
        EventRequest eReq = requestRepository.getReferenceById(reqId);

        eReq.setStatus(RequestState.CONFIRMED);

        eReq = requestRepository.saveAndFlush(eReq);

        return EventRequestMapper.toParticipationRequestDto(eReq);
    }

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя
     *
     * @param userId
     * @param eventId
     * @param reqId
     * @return
     */
    @Override
    public ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long reqId) {
        User user = userService.getUser(userId);
        Event event = repository.getReferenceById(eventId);
        EventRequest eReq = requestRepository.getReferenceById(reqId);

        eReq.setStatus(RequestState.REJECTED);

        eReq = requestRepository.saveAndFlush(eReq);

        return EventRequestMapper.toParticipationRequestDto(eReq);
    }
}
