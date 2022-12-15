package ru.practicum.explore.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.ewm.dto.CommentDto;
import ru.practicum.explore.ewm.mapper.CommentMapper;
import ru.practicum.explore.ewm.model.Comment;
import ru.practicum.explore.ewm.repository.CommentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository repository;

    public PublicCommentServiceImpl(CommentRepository repository) {
        this.repository = repository;
    }

    /**
     * Получение комментариев по отдельному событию
     *
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    @Override
    public Collection<CommentDto> getEventComments(Long eventId, Integer from, Integer size) {
        Pageable pagingSet = PageRequest.of(from / size, size, Sort.by("createdOn").descending());

        Page<Comment> retPage = repository.findAllByEvent_IdAndStatusIsPublished(eventId, pagingSet);

        List<CommentDto> ret = new ArrayList<>();
        for (Comment comment : retPage) {
            ret.add(CommentMapper.toCommentDto(comment));
        }

        return ret;
    }
}
