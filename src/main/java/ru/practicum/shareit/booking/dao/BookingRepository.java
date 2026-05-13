package ru.practicum.shareit.booking.dao;

import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository {

    Booking addNewBooking(NewBookingRequest newBookingRequest, Long userId, Long itemId);

    List<Booking> getBookingsByItemId(Long itemId);

    List<Booking> getBookingsByUserId(Long userId);

}
