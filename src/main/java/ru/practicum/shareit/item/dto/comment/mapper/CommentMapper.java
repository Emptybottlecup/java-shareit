package ru.practicum.shareit.item.dto.comment.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment mapNewCommentRequestToComment(NewCommentRequest newCommentRequest, String authorName, Item item) {
        Comment newComment = new Comment();

        newComment.setItem(item);
        newComment.setText(newCommentRequest.getText());
        newComment.setAuthor(authorName);
        newComment.setCreated(LocalDateTime.now());

        return newComment;
    }

    public CommentDto mapCommentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor());
        commentDto.setCreated(comment.getCreated());

        return commentDto;
    }

}
