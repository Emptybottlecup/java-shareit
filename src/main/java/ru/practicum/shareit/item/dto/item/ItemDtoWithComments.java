package ru.practicum.shareit.item.dto.item;

import lombok.Data;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDtoWithComments {
    private Long id;
    private Long owner;
    private String name;
    private String description;
    private Boolean available;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<CommentDto> comments;
}
