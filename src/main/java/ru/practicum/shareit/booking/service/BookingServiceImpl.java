package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.item.UpdateItemInformation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRep;
    private final UserRepository userRep;
    private final ItemRepository itemRep;
    private final ItemService itemService;

    public BookingDto addNewBooking(NewBookingRequest newBookingRequest, Long userId) {
        Long itemId = newBookingRequest.getItemId();

        checkValidDates(newBookingRequest.getStart(), newBookingRequest.getEnd());

        User booker = userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format("User with id = %d not found", userId)));

        Item item = itemRep.findById(itemId).orElseThrow(() ->
                new NotFoundItemException(String.format("Item with id = %d not found", itemId)));

        if (!item.getAvailable()) {
            log.info("Предмет с id = {} не доступен для бронирования", itemId);
            throw new ItemIsNotAvailable(String.format("Item with id = %d is not available for booking", itemId));
        }

        return BookingMapper.mapBookingToBookingDto(bookingRep.save(BookingMapper.mapNewBookingRequestToBooking(
                newBookingRequest, booker, item)));
    }

    public List<BookingDto> getBookingsByItemId(Long itemId) {
        itemRep.findById(itemId).orElseThrow(() ->
                new NotFoundItemException(String.format("Item with id = %d not found", itemId)));

        return bookingRep.findBookingsByItemId(itemId)
                .stream()
                .map(BookingMapper::mapBookingToBookingDto)
                .toList();
    }

    public List<BookingDto> getBookingsByUserId(Long userId, String state) {
        userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format("User with id = %d not found", userId)));

        BooleanExpression expression = QBooking.booking.booker.id.eq(userId);

        expression = chooseBooleanExpression(state, expression);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        return StreamSupport.stream(bookingRep.findAll(expression, sort).spliterator(), false)
                .map(BookingMapper::mapBookingToBookingDto)
                .toList();
    }

    public List<BookingDto> getBookingsByOwnerId(Long userId, String state) {
        userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format("User with id = %d not found", userId)));

        BooleanExpression expression = QBooking.booking.item.owner.id.eq(userId);

        expression = chooseBooleanExpression(state, expression);

        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        return StreamSupport.stream(bookingRep.findAll(expression, sort).spliterator(), false)
                .map(BookingMapper::mapBookingToBookingDto)
                .toList();
    }

    public BookingDto getBookingById(Long bookingId, Long userId) {
        userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format("User with id = %d not found", userId)));

        Booking booking = bookingRep.findById(bookingId).orElseThrow(() ->
                new NotFoundBookingException(String.format("Booking with id = %d not found", bookingId)));

        if (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwner().getId().equals(userId)) {
            throw new AccessException(String.format("User with id = %d doesnt have access to watch information about "
                    + "booking with id = %d", userId, bookingId));
        }

        return BookingMapper.mapBookingToBookingDto(booking);
    }

    public BookingDto changeBookingStatus(Long userId, Long bookingId, boolean isApproved) {
        userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserForBookingsException(String.format("User with id = %d not found", userId)));

        Booking booking = bookingRep.findById(bookingId).orElseThrow(() ->
                new NotFoundBookingException(String.format("Booking with id = %d not found", bookingId)));

        if (isApproved) {
            booking.setStatus(BookingStatus.APPROVED);
            UpdateItemInformation updateItemInformation = new UpdateItemInformation();
            updateItemInformation.setAvailable(false);
            itemService.updateItemInformation(updateItemInformation, userId, booking.getItem().getId());
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.mapBookingToBookingDto(bookingRep.save(booking));
    }

    private void checkValidDates(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException("start time booking is after end time booking");
        }
        if (end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("end time booking in past");
        }
        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("start time booking in past");
        }
        if (start.equals(end)) {
            throw new ValidationException("start time booking is equal end time booking");
        }
    }

    private BooleanExpression chooseBooleanExpression(String state, BooleanExpression expression) {
        LocalDateTime currentTime = LocalDateTime.now();

        switch (state) {
            case "ALL": {
                break;
            }
            case "CURRENT": {
                BooleanExpression startLessEqualNow = QBooking.booking.start.loe(currentTime);
                BooleanExpression endGreaterNow = QBooking.booking.end.gt(currentTime);
                expression = expression.and(startLessEqualNow).and(endGreaterNow);
                break;
            }
            case "PAST": {
                BooleanExpression endLessNow = QBooking.booking.end.lt(currentTime);
                expression = expression.and(endLessNow);
                break;
            }
            case "FUTURE": {
                BooleanExpression startGreaterNow = QBooking.booking.start.gt(currentTime);
                expression = expression.and(startGreaterNow);
                break;
            }
            case "WAITING": {
                BooleanExpression waitingStatus = QBooking.booking.status.eq(BookingStatus.WAITING);
                expression = expression.and(waitingStatus);
                break;
            }
            case "REJECTED": {
                BooleanExpression rejectedStatus = QBooking.booking.status.eq(BookingStatus.REJECTED);
                expression = expression.and(rejectedStatus);
                break;
            }
            default: {
                throw new ValidationException(String.format("This state = %s is not exist", state));
            }
        }

        return expression;
    }

}
