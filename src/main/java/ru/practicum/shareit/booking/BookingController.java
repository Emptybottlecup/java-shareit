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

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getBookingsByUserId(@RequestHeader(HEADER_USER_ID) Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getBookingsByUserId(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestHeader(HEADER_USER_ID) Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getBookingsByUserId(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(HEADER_USER_ID) Long userId, @PathVariable Long bookingId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @PostMapping
    public BookingDto addNewBooking(@RequestBody @Valid NewBookingRequest newBookingRequest,
                                    @RequestHeader(HEADER_USER_ID) Long userId) {
        return bookingService.addNewBooking(newBookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto changeBooingStatus(@RequestHeader(HEADER_USER_ID) Long userId, @PathVariable Long bookingId,
                                   @RequestParam(name = "approved") boolean isApproved) {
        return bookingService.changeBookingStatus(userId, bookingId, isApproved);
    }

}
