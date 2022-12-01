package ru.practicum.explore.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.ewm.model.Event;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Page<Event> getAllByInitiator_Id(Long userId, Pageable pagingSet);

    Event findAllByInitiator_IdAndId(Long userId, Long eventId);
}
