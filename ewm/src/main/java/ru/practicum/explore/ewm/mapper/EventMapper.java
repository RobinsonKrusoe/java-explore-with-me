package ru.practicum.explore.ewm.mapper;

import ru.practicum.explore.ewm.dto.*;
import ru.practicum.explore.ewm.model.Category;
import ru.practicum.explore.ewm.model.Event;
import ru.practicum.explore.ewm.model.EventState;
import ru.practicum.explore.ewm.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    public static EventFullDto toEventFullDto(Event event) {
        if (event != null) {
            EventFullDto ret = EventFullDto.builder()
                    .id(event.getId())
                    .annotation(event.getAnnotation())
                    .category(CategoryMapper.toCategoryDto(event.getCategory()))
                    .confirmedRequests(event.getConfirmedRequests())
                    .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .description(event.getDescription())
                    .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                    .location(Location.builder().lat(event.getLat()).lon(event.getLon()).build())
                    .paid(event.getPaid())
                    .participantLimit(event.getParticipantLimit())
                    .requestModeration(event.getRequestModeration())
                    .state(event.getState())
                    .title(event.getTitle())
                    .views(event.getViews())
                    .build();
            if (event.getPublishedOn() != null) {
                ret.setPublishedOn(event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            return ret;
        } else {
            return null;
        }
    }

    public static EventShortDto toEventShortDto(Event event) {
        if (event != null) {
            return EventShortDto.builder()
                    .id(event.getId())
                    .annotation(event.getAnnotation())
                    .category(CategoryMapper.toCategoryDto(event.getCategory()))
                    .confirmedRequests(event.getConfirmedRequests())
                    .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                    .paid(event.getPaid())
                    .title(event.getTitle())
                    .views(event.getViews())
                    .build();
        } else {
            return null;
        }
    }

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category) {
        if (newEventDto != null) {
            Event event = new Event();
            event.setAnnotation(newEventDto.getAnnotation());
            event.setCategory(category);
            event.setCreatedOn(LocalDateTime.now());
            event.setDescription(newEventDto.getDescription());
            event.setEventDate(newEventDto.getEventDate());
            event.setInitiator(initiator);
            event.setLat(newEventDto.getLocation().getLat());
            event.setLon(newEventDto.getLocation().getLon());
            event.setPaid(newEventDto.getPaid());
            event.setParticipantLimit(newEventDto.getParticipantLimit());
            event.setRequestModeration(newEventDto.getRequestModeration());
            event.setState(EventState.PENDING);
            event.setTitle(newEventDto.getTitle());
            return event;
        } else {
            return null;
        }
    }


}
