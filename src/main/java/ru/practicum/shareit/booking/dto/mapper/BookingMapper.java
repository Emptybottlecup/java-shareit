package ru.practicum.shareit.booking.dto.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static Booking mapNewBookingRequestToBooking(NewBookingRequest newBookingRequest, Long bookerId,
                                                        Long itemId) {
        Booking bookingDto = new Booking();

        bookingDto.setItemId(itemId);
        bookingDto.setBookerId(bookerId);
        bookingDto.setStartBooking(newBookingRequest.getStartBooking());
        bookingDto.setEndBooking(newBookingRequest.getEndBooking());

        return bookingDto;
    }

    public static BookingDto mapBookingToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setItemId(booking.getItemId());
        bookingDto.setBookerId(booking.getBookerId());
        bookingDto.setStartBooking(booking.getStartBooking());
        bookingDto.setEndBooking(booking.getEndBooking());

        return bookingDto;
    }

}
