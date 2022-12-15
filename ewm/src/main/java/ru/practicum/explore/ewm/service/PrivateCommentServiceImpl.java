package ru.practicum.explore.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.explore.errorHandle.exception.EntityNotFoundException;
import ru.practicum.explore.errorHandle.exception.ValidationException;
import ru.practicum.explore.ewm.dto.CommentDto;
import ru.practicum.explore.ewm.mapper.CommentMapper;
import ru.practicum.explore.ewm.model.*;
import ru.practicum.explore.ewm.repository.CommentRepository;
import ru.practicum.explore.ewm.repository.EventRepository;
import ru.practicum.explore.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public PrivateCommentServiceImpl(CommentRepository repository,
                                     UserRepository userRepository,
                                     EventRepository eventRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    /**
     * Добавление нового комментария
     * Комментирование доступно всем пользователям для возможности сообщить о накладках мероприятия,
     * непозволивших его посетить
     *
     * @param userId
     * @param commentDto
     * @return
     */
    @Override
    @Transactional
    public CommentDto addComment(Long userId, CommentDto commentDto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь #" + userId + " не существует!"));

        Event event = eventRepository.findById(commentDto.getEvent())
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие #" + commentDto.getEvent() + " не найдено!"));

        Comment newComment = CommentMapper.toComment(commentDto, author, event);

        repository.save(newComment);

        return CommentMapper.toCommentDto(newComment);
    }

    /**
     * Изменение комментария
     * Может делать только автор
     *
     * @param userId
     * @param commentDto
     * @return
     */
    @Override
    @Transactional
    public CommentDto changeComment(Long userId, CommentDto commentDto) {
        Comment commentDB = getComment(commentDto.getId());

        if (commentDB.getStatus() != CommentState.PUBLISHED) {
            throw new ValidationException("Можно редактировать комментарии только в статусе " +
                    CommentState.PUBLISHED + "!");
        }

        if (commentDB.getAuthor().getId() != userId) {
            throw new ValidationException("Можно редактировать только свои комментарии!");
        }

        commentDB.setText(commentDto.getText());
        commentDB.setChangedOn(LocalDateTime.now());

        repository.save(commentDB);

        return CommentMapper.toCommentDto(commentDB);
    }

    /**
     * Отмена комментария
     * Если отменяет автор, комментарий переходит в статус CANCELED
     * Если отменяет создатель события, комментарий переходит в статус BLOCKED
     *
     * @param userId
     * @param commentId
     * @return
     */
    @Override
    @Transactional
    public CommentDto cancelComment(Long userId, Long commentId) {
        Comment commentDB = getComment(commentId);

        if (commentDB.getStatus() != CommentState.PUBLISHED) {
            throw new ValidationException("Можно отменять комментарии только в статусе " +
                    CommentState.PUBLISHED + "!");
        }

        if (commentDB.getAuthor().getId() == userId) {
            commentDB.setStatus(CommentState.CANCELED);
        } else if (commentDB.getEvent().getInitiator().getId() == userId) {
            commentDB.setStatus(CommentState.BLOCKED);
        } else {
            throw new ValidationException("Можно отменять только свои комментарии, " +
                                          "либо комментании на свои мероприятия!");
        }

        commentDB.setChangedOn(LocalDateTime.now());

        repository.save(commentDB);

        return CommentMapper.toCommentDto(commentDB);
    }

    /**
     * Получение своих комментариев
     *
     * @param userId
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    @Override
    public Collection<CommentDto> getOwnComments(Long userId, Long eventId, Integer from, Integer size) {
        Pageable pagingSet = PageRequest.of(from / size, size, Sort.by("createdOn").descending());

        Specification<Comment> spec = Specification.where(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("author").get("id"), userId)
        );

        if (eventId != null) {
            spec = Specification.where(spec).and(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get("event").get("id"),eventId)
            );
        }

        Page<Comment> retPage = repository.findAll(spec, pagingSet);

        List<CommentDto> ret = new ArrayList<>();
        for (Comment comment : retPage) {
            ret.add(CommentMapper.toCommentDto(comment));
        }

        return ret;
    }

    /**
     * Получение комментариев по отдельному событию
     *
     * @param userId
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    @Override
    public Collection<CommentDto> getEventComments(Long userId, Long eventId, Integer from, Integer size) {
        Pageable pagingSet = PageRequest.of(from / size, size, Sort.by("created_on").descending());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Мероприятие #" + eventId + " не найдено!"));

        Page<Comment> retPage = repository.findAllByEventIdWithHiding(userId, eventId, pagingSet);

        List<CommentDto> ret = new ArrayList<>();
        for (Comment comment : retPage) {
            ret.add(CommentMapper.toCommentDto(comment));
        }

        return ret;
    }

    private Comment getComment(Long commentId) {
        return repository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий #" + commentId + " не найден!"));
    }
}
