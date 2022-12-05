package ru.practicum.explore.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.explore.ewm.model.Event;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Page<Event> getAllByInitiator_Id(Long userId, Pageable pagingSet);

    boolean existsByCategory_Id(Long catId);

    Optional<Event> findByInitiator_IdAndId(Long userId, Long eventId);

    Set<Event> findByIdIn(List<Long> events);
}
