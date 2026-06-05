package ru.practicum.shareit.booking.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {

    public static Booking mapNewBookingRequestToBooking(NewBookingRequest newBookingRequest, User booker,
                                                        Item item) {
        Booking booking = new Booking();

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStart(newBookingRequest.getStart());
        booking.setEnd(newBookingRequest.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return booking;
    }

    public static BookingDto mapBookingToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setItem(ItemMapper.mapItemToItemDtoWithoutComments(booking.getItem()));
        bookingDto.setBooker(UserMapper.mapUserToUserDto(booking.getBooker()));
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());

        return bookingDto;
    }

}
