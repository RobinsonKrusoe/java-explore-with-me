package ru.practicum.explore.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.ewm.model.Comment;
import ru.practicum.explore.ewm.model.CommentState;

public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    default Page<Comment> findAllByEvent_IdAndStatusIsPublished(Long eventId, Pageable pagingSet) {
        return findAllByEvent_IdAndStatus(eventId, CommentState.PUBLISHED, pagingSet);
    }

    Page<Comment> findAllByEvent_IdAndStatus(Long eventId, CommentState state, Pageable pagingSet);

    @Query(value = " select c.* from comments c, events e " +
                    " where c.event_id = e.id " +
                      " and c.event_id = ?2 " +
                      " and (c.status = 'PUBLISHED' or c.author_id = ?1 or e.initiator_id = ?1)",
            nativeQuery = true)
    Page<Comment> findAllByEventIdWithHiding(Long userId, Long eventId, Pageable pagingSet);


}