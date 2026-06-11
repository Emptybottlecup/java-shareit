package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.item.ItemDtoWithoutComments;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    private Long id;
    private UserDto booker;
    private ItemDtoWithoutComments item;
    private BookingStatus status;
    private LocalDateTime start;
    private LocalDateTime end;
}
