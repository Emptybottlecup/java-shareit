package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.List;

public interface BookingService {

    BookingDto addNewBooking(NewBookingRequest newBookingRequest, Long userId, Long itemId);

    List<BookingDto> getBookingsByItemId(Long itemId);

    List<BookingDto> getBookingsByUserId(Long itemId);

}
