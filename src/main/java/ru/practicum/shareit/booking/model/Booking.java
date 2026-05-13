package ru.practicum.shareit.booking.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    private Long bookerId;
    private Long itemId;
    private LocalDate startBooking;
    private LocalDate endBooking;
}
