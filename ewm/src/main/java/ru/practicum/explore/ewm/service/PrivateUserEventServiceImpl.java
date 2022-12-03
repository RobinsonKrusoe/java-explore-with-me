package ru.practicum.explore.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.*;
import ru.practicum.explore.ewm.mapper.EventMapper;
import ru.practicum.explore.ewm.mapper.EventRequestMapper;
import ru.practicum.explore.ewm.model.*;
import ru.practicum.explore.ewm.repository.CategoryRepository;
import ru.practicum.explore.ewm.repository.EventRepository;
import ru.practicum.explore.ewm.repository.EventRequestRepository;
import ru.practicum.explore.ewm.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PrivateUserEventServiceImpl implements PrivateUserEventService {
    private final EventRepository repository;
    private final EventRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsService statsService;

    public PrivateUserEventServiceImpl(EventRepository repository,
                                       EventRequestRepository requestRepository,
                                       UserRepository userRepository,
                                       CategoryRepository categoryRepository,
                                       StatsService statsService) {
        this.repository = repository;
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
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
        checkUser(userId);

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
    @Transactional
    public EventFullDto patchEvent(Long userId, UpdateEventRequestDto updateEventRequestDto) {
        checkUser(userId);

        if (updateEventRequestDto.getEventId() == null) {
            throw new ValidationException("Не задан идентификатор события для изменений!");
        }

        Event eventDB = repository.getReferenceById(updateEventRequestDto.getEventId());

        if (updateEventRequestDto.getAnnotation() != null) {
            eventDB.setAnnotation(updateEventRequestDto.getAnnotation());
        }

        if (updateEventRequestDto.getCategory() != null) {
            Category category = getCategory(updateEventRequestDto.getCategory());
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

        repository.save(eventDB);

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
    @Transactional
    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getAnnotation() == null ||
            newEventDto.getCategory() == null ||
            newEventDto.getDescription() == null ||
            newEventDto.getEventDate() == null ||
            newEventDto.getLocation() == null ||
            newEventDto.getTitle() == null) {
            throw new ValidationException("Некорректное событие");
        }

        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь #" + userId + " не существует!"));
        Category category = getCategory(newEventDto.getCategory());

        Event newEvent = EventMapper.toEvent(newEventDto, initiator, category);

        repository.save(newEvent);

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
        Event event = repository.findByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие #" + eventId +
                        " у пользователя " + userId + " не найдено!"));;

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
    @Transactional
    public EventFullDto canselOwnEvent(Long userId, Long eventId) {
        Event eventDB = repository.findByInitiator_IdAndId(userId, eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие #" + eventId +
                        " у пользователя " + userId + " не найдено!"));

        if (eventDB.getState() == EventState.PENDING) {
            eventDB.setState(EventState.CANCELED);
        } else {
            throw new ValidationException("Отменять можно только события в статусе " + EventState.PENDING);
        }

        repository.save(eventDB);

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
    @Transactional
    public ParticipationRequestDto confirmParticipationRequest(Long userId, Long eventId, Long reqId) {
        EventRequest eReq = requestRepository.getReferenceById(reqId);

        if (eReq.getEvent().getInitiator().getId() != userId) {
            throw new ValidationException("Можно подтверждать только заявки на своё мероприятие!");
        }

        if (eReq.getEvent().getParticipantLimit() > 0) {    //Если установлен лимит заявок
            Integer reqCount = requestRepository.countAllByEventId(eventId);
            if (eReq.getEvent().getParticipantLimit() <= reqCount) {
                throw new ValidationException("Лимит заявок на данное мероприятие исчерпан!");
            }
        }

        eReq.setStatus(RequestState.CONFIRMED);

        requestRepository.save(eReq);

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
    @Transactional
    public ParticipationRequestDto rejectParticipationRequest(Long userId, Long eventId, Long reqId) {
        EventRequest eReq = requestRepository.getReferenceById(reqId);

        if (eReq.getEvent().getInitiator().getId() != userId) {
            throw new ValidationException("Можно отменять заявки только на своё мероприятие!");
        }

        eReq.setStatus(RequestState.REJECTED);

        requestRepository.save(eReq);

        return EventRequestMapper.toParticipationRequestDto(eReq);
    }

    private void checkUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Пользователь #" + userId + " не найден!");
        }
    }

    private Category getCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException("Категория #" + catId + " не существует!"));
    }
}
