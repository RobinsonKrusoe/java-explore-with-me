package ru.practicum.explore.ewm.mapper;

import ru.practicum.explore.ewm.dto.*;
import ru.practicum.explore.ewm.model.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        if (comment != null) {
            CommentDto ret = CommentDto.builder()
                    .id(comment.getId())
                    .event(comment.getEvent().getId())
                    .author(UserMapper.toUserShortDto(comment.getAuthor()))
                    .createdOn(comment.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .text(comment.getText())
                    .status(comment.getStatus())
                    .build();

            if (comment.getChangedOn() != null) {
                ret.setChangedOn(comment.getChangedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            return ret;
        } else {
            return null;
        }
    }

    public static Comment toComment(CommentDto commentDto, User author, Event event) {
        if (commentDto != null) {
            Comment comment = new Comment();
            comment.setEvent(event);
            comment.setAuthor(author);
            comment.setCreatedOn(LocalDateTime.now());
            comment.setText(commentDto.getText());
            comment.setStatus(CommentState.PUBLISHED);
            return comment;
        } else {
            return null;
        }
    }


}
