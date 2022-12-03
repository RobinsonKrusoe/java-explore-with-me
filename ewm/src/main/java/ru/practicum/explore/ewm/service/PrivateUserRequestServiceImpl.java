package ru.practicum.explore.ewm.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.mapper.EventRequestMapper;
import ru.practicum.explore.ewm.model.*;
import ru.practicum.explore.ewm.repository.EventRepository;
import ru.practicum.explore.ewm.repository.EventRequestRepository;
import ru.practicum.explore.ewm.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivateUserRequestServiceImpl implements PrivateUserRequestService {
    private final EventRequestRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public PrivateUserRequestServiceImpl(EventRequestRepository repository,
                                         UserRepository userRepository,
                                         EventRepository eventRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param userId
     * @return
     */
    @Override
    public List<ParticipationRequestDto> get(Long userId) {
        List<ParticipationRequestDto> ret = new ArrayList<>();
        for (EventRequest eReq : repository.findAllOwnRequests(userId)) {
            ret.add(EventRequestMapper.toParticipationRequestDto(eReq));
        }

        return ret;
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     *
     * @param userId  id текущего пользователя
     * @param eventId
     * @return
     */
    @Override
    @Transactional
    public ParticipationRequestDto addRequestToEvent(Long userId, Long eventId) {
        if (repository.existsByRequester_IdAndEvent_Id(userId, eventId)) {
            throw new ValidationException("Запрос пользователя #" + userId +
                    " на мероприятие #" + eventId + " уже существует!");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Событие #" + eventId + " не существует!"));

        if (event.getInitiator().getId() == userId) {
            throw new ValidationException("Нельзя посылать запрос на своё мероприятие!");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ValidationException("Можно посылать заявки только на мероприятия в статусе " +
                    EventState.PUBLISHED + "!");
        }

        if (event.getParticipantLimit() > 0) {    //Если установлен лимит заявок
            Integer reqCount = repository.countAllByEventId(eventId);
            if (event.getParticipantLimit() <= reqCount) {
                throw new ValidationException("Лимит заявок на данное мероприятие исчерпан!");
            }
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь #" + userId + " не существует!"));

        EventRequest newEventRequest = EventRequestMapper.toNewEventRequest(user, event);

        repository.save(newEventRequest);

        return EventRequestMapper.toParticipationRequestDto(newEventRequest);
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId
     * @param requestId
     * @return
     */
    @Override
    @Transactional
    public ParticipationRequestDto canselOwnRequest(Long userId, Long requestId) {
        EventRequest eventRequestDB = repository.findByRequester_idAndId(userId, requestId);
        eventRequestDB.setStatus(RequestState.CANCELED);

        repository.save(eventRequestDB);

        return EventRequestMapper.toParticipationRequestDto(eventRequestDB);
    }
}
