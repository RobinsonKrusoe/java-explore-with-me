package ru.practicum.explore.ewm.service;

import org.springframework.stereotype.Component;
import ru.practicum.explore.ewm.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.mapper.EventRequestMapper;
import ru.practicum.explore.ewm.model.*;
import ru.practicum.explore.ewm.repository.EventRequestRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class PrivateUserRequestServiceImpl implements PrivateUserRequestService {
    private final EventRequestRepository repository;
    private final AdminUserService userService;
    private final AdminEventService eventService;

    public PrivateUserRequestServiceImpl(EventRequestRepository repository,
                                         AdminUserService userService,
                                         AdminEventService eventService) {
        this.repository = repository;
        this.userService = userService;
        this.eventService = eventService;
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param userId
     * @return
     */
    @Override
    public List<ParticipationRequestDto> get(Long userId) {
        User user = userService.getUser(userId);

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
    public ParticipationRequestDto addRequestToEvent(Long userId, Long eventId) {
        User user = userService.getUser(userId);
        Event event = eventService.get(eventId);

        EventRequest newEventRequest = EventRequestMapper.toNewEventRequest(user, event);

        newEventRequest = repository.saveAndFlush(newEventRequest);

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
    public ParticipationRequestDto canselOwnRequest(Long userId, Long requestId) {
        User user = userService.getUser(userId);
        EventRequest eventRequestDB = repository.getReferenceById(requestId);
        eventRequestDB.setStatus(RequestState.CANCELED);
        eventRequestDB = repository.saveAndFlush(eventRequestDB);

        return EventRequestMapper.toParticipationRequestDto(eventRequestDB);
    }
}
