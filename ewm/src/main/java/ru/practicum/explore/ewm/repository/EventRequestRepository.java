package ru.practicum.explore.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.explore.ewm.model.EventRequest;

import java.util.List;

public interface EventRequestRepository extends JpaRepository<EventRequest, Long> {

    @Query(value = " select er.* from event_request er, events e " +
            " where er.requester_id = ?1 " +
            " and er.event_id = e.id " +
            " and e.initiator_id <> ?1 ",
            nativeQuery = true)
    List<EventRequest> findAllOwnRequests(Long userId);

    List<EventRequest> findAllByEvent_Initiator_IdAndEvent_Id(Long userId, Long eventId);
}