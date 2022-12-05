package ru.practicum.explore.ewm.mapper;

import ru.practicum.explore.ewm.dto.*;
import ru.practicum.explore.ewm.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(EventRequest eventRequest) {
        if (eventRequest != null) {
            return ParticipationRequestDto.builder()
                    .id(eventRequest.getId())
                    .created(eventRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .event(eventRequest.getEvent().getId())
                    .requester(eventRequest.getRequester().getId())
                    .status(eventRequest.getStatus().name())
                    .build();
        } else {
            return null;
        }
    }

    public static EventRequest toNewEventRequest(User user, Event event) {
        EventRequest eventRequest = new EventRequest();
        eventRequest.setEvent(event);
        eventRequest.setRequester(user);
        eventRequest.setCreated(LocalDateTime.now());
        eventRequest.setStatus(RequestState.PENDING);

        return eventRequest;
    }
}
