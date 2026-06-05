package ru.practicum.shareit.item.dto.comment;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
