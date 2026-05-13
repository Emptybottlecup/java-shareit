package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class NewBookingRequest {
    @NotNull
    private LocalDate startBooking;
    @NotNull
    private LocalDate endBooking;
}
