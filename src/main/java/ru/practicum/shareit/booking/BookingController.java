package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final static String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getBookingsByUserId(@RequestHeader(HEADER_USER_ID) Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public List<BookingDto> getBookingsByItemId(@PathVariable Long itemId) {
        return bookingService.getBookingsByItemId(itemId);
    }

    @PostMapping("/{itemId}")
    public BookingDto addNewBooking(@RequestBody @Valid NewBookingRequest newBookingRequest,
                                    @RequestHeader(HEADER_USER_ID) Long userId, @PathVariable Long itemId) {
        return bookingService.addNewBooking(newBookingRequest, userId, itemId);
    }

}
