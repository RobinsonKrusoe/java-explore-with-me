package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.dto.CommentDto;
import ru.practicum.explore.ewm.service.PublicCommentService;

import java.util.Collection;

/**
 * Публичный API для работы с комментариями
 */
@Slf4j
@RestController
@RequestMapping(path = "/comments")
public class PublicCommentsController {
    private final PublicCommentService service;

    public PublicCommentsController(PublicCommentService service) {
        this.service = service;
    }

    @GetMapping("/{eventId}")
    public Collection<CommentDto> getComments(@PathVariable Long eventId,
                                              @RequestParam(required = false, defaultValue = "0") Integer from,
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get Comments for eventId={}, from={}, size={}", eventId, from, size);
        return service.getEventComments(eventId, from, size);
    }
}
