package ru.practicum.explore.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.practicum.explore.ewm.dto.CommentDto;
import ru.practicum.explore.ewm.service.PrivateCommentService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * Публичный API для работы с комментариями
 */
@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/comments")
public class PrivateCommentController {
    private final PrivateCommentService service;

    public PrivateCommentController(PrivateCommentService service) {
        this.service = service;
    }

    /**
     * Добавление нового комментария
     * Комментирование доступно всем пользователям для возможности сообщить о накладках мероприятия,
     * непозволивших его посетить
     * @param userId
     * @param comment
     * @return
     */
    @PostMapping
    public CommentDto addComment(@PathVariable Long userId,
                                 @Valid @RequestBody CommentDto comment) {
        log.info("Add Comment with userId={}, comment {}", userId, comment);
        return service.addComment(userId, comment);
    }

    /**
     * Изменение комментария
     * Может делать только автор
     * @param userId
     * @param comment
     * @return
     */
    @PutMapping
    public CommentDto changeComment(@PathVariable Long userId,
                                    @Valid @RequestBody CommentDto comment) {
        log.info("Change Comment with userId={}, comment {}", userId, comment);
        return service.changeComment(userId, comment);
    }

    /**
     * Отмена комментария
     * Если отменяет автор, комментарий переходит в статус CANCELED
     * Если отменяет создатель события, комментарий переходит в статус BLOCKED
     * @param userId
     * @param commentId
     * @return
     */
    @PatchMapping("/{commentId}/cancel")
    public CommentDto cancelComment(@PathVariable Long userId,
                                      @PathVariable Long commentId) {

        log.info("Cancel Comment with userId={}, commentId={}", userId, commentId);
        return service.cancelComment(userId, commentId);
    }

    /**
     * Получение своих комментариев
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    @GetMapping("/own")
    public Collection<CommentDto> getOwnComments(@PathVariable Long userId,
                                                 @RequestParam(required = false) Long eventId,
                                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get Comments for userId={}, eventId={}, from={}, size={}", userId, eventId, from, size);
        return service.getOwnComments(userId, eventId, from, size);
    }

    /**
     * Получение комментариев по отдельному событию
     * @param eventId
     * @param from
     * @param size
     * @return
     */
    @GetMapping("/event/{eventId}")
    public Collection<CommentDto> getEventComments(@PathVariable Long userId,
                                                   @PathVariable Long eventId,
                                                   @RequestParam(required = false, defaultValue = "0") Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get Event Comments for userId={}, eventId={}, from={}, size={}", userId, eventId, from, size);
        return service.getEventComments(userId, eventId, from, size);
    }
}
