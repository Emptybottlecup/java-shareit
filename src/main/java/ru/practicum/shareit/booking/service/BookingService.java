package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.util.List;

public interface BookingService {

    BookingDto addNewBooking(NewBookingRequest newBookingRequest, Long userId);

    List<BookingDto> getBookingsByUserId(Long userId, String state);

    BookingDto getBookingById(Long bookingId, Long userId);

    BookingDto changeBookingStatus(Long userId, Long bookingId, boolean isApproved);

}
