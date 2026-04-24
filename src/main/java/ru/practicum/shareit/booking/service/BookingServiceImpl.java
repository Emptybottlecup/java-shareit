package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.exceptions.ItemIsNotAvailable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemInformation;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRep;
    private final UserService userService;
    private final ItemService itemService;

    public BookingDto addNewBooking(NewBookingRequest newBookingRequest, Long userId, Long itemId) {
        userService.getUser(userId);
        ItemDto itemDto = itemService.getItem(itemId);

        if (!itemDto.getAvailable() && !checkFreePeriod(newBookingRequest, itemId)) {
            log.info("Предмет с id = {} не доступен для бронирования", itemId);
            throw new ItemIsNotAvailable(String.format("Item with id = %d is not available for booking", itemId));
        }

        UpdateItemInformation updateItemInformation = new UpdateItemInformation();
        updateItemInformation.setAvailable(false);
        itemService.updateItemInformation(updateItemInformation, itemDto.getOwner(), itemId);
        return BookingMapper.mapBookingToBookingDto(bookingRep.addNewBooking(newBookingRequest, userId, itemId));
    }

    public List<BookingDto> getBookingsByItemId(Long itemId) {
        itemService.getItem(itemId);
        return bookingRep.getBookingsByItemId(itemId)
                .stream()
                .map(BookingMapper::mapBookingToBookingDto)
                .toList();
    }

    public List<BookingDto> getBookingsByUserId(Long userId) {
        itemService.getUserItems(userId);
        return bookingRep.getBookingsByUserId(userId)
                .stream()
                .map(BookingMapper::mapBookingToBookingDto)
                .toList();
    }

    private boolean checkFreePeriod(NewBookingRequest request, Long itemId) {
        return bookingRep.getBookingsByItemId(itemId)
                .stream()
                .noneMatch(b -> request.getStartBooking().isBefore(b.getEndBooking())
                        && request.getEndBooking().isAfter(b.getStartBooking()));
    }

}
