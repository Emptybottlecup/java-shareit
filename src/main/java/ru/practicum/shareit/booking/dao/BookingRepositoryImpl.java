package ru.practicum.shareit.booking.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final Map<Long, Booking> bookings;
    private Long currentId = 0L;

    public Booking addNewBooking(NewBookingRequest newBookingRequest, Long userId, Long itemId) {
        Booking booking = BookingMapper.mapNewBookingRequestToBooking(newBookingRequest, userId, itemId);
        booking.setId(generateNewId());
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public List<Booking> getBookingsByItemId(Long itemId) {
        return bookings.values()
                .stream()
                .filter(booking -> booking.getItemId().equals(itemId))
                .toList();
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookings.values()
                .stream()
                .filter(booking -> booking.getItemId().equals(userId))
                .toList();
    }

    private Long generateNewId() {
        return ++currentId;
    }

}
