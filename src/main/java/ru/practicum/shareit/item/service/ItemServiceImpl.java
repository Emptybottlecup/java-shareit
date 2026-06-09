package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.CommentAddException;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.dto.item.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.item.ItemDtoWithoutComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemInformation;
import ru.practicum.shareit.item.dto.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QComment;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRep;
    private final UserRepository userRep;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public ItemDtoWithoutComments addNewItem(NewItemRequest newItemRequest, Long userId) {
        User owner = userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format("User with id = %d not exist", userId)));

        Item newItem = ItemMapper.mapNewItemRequestToItem(newItemRequest, owner);

        return ItemMapper.mapItemToItemDtoWithoutComments(itemRep.save(newItem));
    }

    public List<ItemDtoWithComments> getUserItems(Long userId) {
        List<Item> userItems = itemRep.findByOwnerId(userId);

        List<Long> itemsId = userItems.stream().map(Item::getId).toList();

        BooleanExpression expressionBookings = QBooking.booking.item.id.in(itemsId);
        Map<Long, List<Booking>> bookingsByItemId = StreamSupport.stream(bookingRepository.findAll(expressionBookings)
                .spliterator(), false).collect(Collectors.groupingBy(booking -> booking
                .getItem().getId()));

        BooleanExpression expressionComments = QComment.comment.item.id.in(itemsId);
        Map<Long, List<Comment>> commentsById = StreamSupport.stream(commentRepository.findAll(expressionComments)
                .spliterator(), false).collect(Collectors.groupingBy(comment -> comment
                .getItem().getId()));

        return userItems.stream()
                .map(item -> {
                    List<CommentDto> comments = commentsById.getOrDefault(item.getId(), new ArrayList<>()).stream()
                            .map(CommentMapper::mapCommentToCommentDto)
                            .toList();

                    ItemDtoWithComments itemDtoWithComments = ItemMapper.mapItemToItemDtoWithComments(item, comments);

                    Booking lastBooking = bookingsByItemId.getOrDefault(item.getId(), new ArrayList<>()).stream()
                            .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) ||
                                    booking.getStart().isEqual(LocalDateTime.now()))
                            .max(Comparator.comparing(Booking::getStart))
                            .orElse(null);

                    Booking nearestBooking = bookingsByItemId.getOrDefault(item.getId(), new ArrayList<>()).stream()
                            .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                            .min(Comparator.comparing(Booking::getStart))
                            .orElse(null);

                    if (lastBooking != null) {
                        itemDtoWithComments.setLastBooking(lastBooking.getStart());
                    }

                    if (nearestBooking != null) {
                        itemDtoWithComments.setNextBooking(nearestBooking.getStart());
                    }

                    return itemDtoWithComments;
                }).toList();
    }

    public ItemDtoWithComments getItem(Long itemId) {
        Item item = itemRep.findById(itemId).orElseThrow(() ->
                new NotFoundItemException(String.format("Item with id = %d not found", itemId)));

        List<CommentDto> comments = commentRepository.findByItemId(item.getId()).stream()
                .map(CommentMapper::mapCommentToCommentDto)
                .toList();

        return ItemMapper.mapItemToItemDtoWithComments(item, comments);
    }

    public ItemDtoWithoutComments updateItemInformation(UpdateItemInformation updateItemInformation, Long userId, Long itemId) {
        Optional<Item> item = itemRep.findById(itemId);

        if (item.isEmpty()) {
            throw new NotFoundItemException(String.format("Item with id = %d not found", itemId));
        }
        if (!item.get().getOwner().getId().equals(userId)) {
            log.info("Пользователь с id = {} не является владельцем item с id = {}", userId, itemId);
            throw new AccessException(String.format("This user id = %d dont have item id = %d", userId, itemId));
        }

        Item updatedItem = ItemMapper.mapUpdateItemInformationToItem(updateItemInformation, item.get());

        itemRep.save(updatedItem);

        return ItemMapper.mapItemToItemDtoWithoutComments(updatedItem);
    }

    public List<ItemDtoWithoutComments> searchItem(String searchText) {
        if (searchText.isBlank()) {
            return new ArrayList<>();
        }

        return itemRep.searchItemsByText(searchText)
                .stream()
                .map(ItemMapper::mapItemToItemDtoWithoutComments)
                .toList();
    }

    public CommentDto addComment(NewCommentRequest newCommentRequest, Long userId, Long itemId) {
        User user = userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format("User with id = %d not exist", userId)));
        Item item = itemRep.findById(itemId).orElseThrow(() ->
                new NotFoundItemException(String.format("Item with id = %d not found", itemId)));

        List<Booking> bookings = bookingRepository.findByBookerIdAndItemId(userId, itemId);

        boolean isHasBeenApproved = false;

        for (var book : bookings) {
            if (book.getStatus().equals(BookingStatus.APPROVED) && book.getEnd().isBefore(LocalDateTime.now())) {
                isHasBeenApproved = true;
                break;
            }
        }

        if (!isHasBeenApproved) {
            throw new CommentAddException(String.format("User with id = %d didnt book item with id = %d", userId,
                    itemId));
        }

        return CommentMapper.mapCommentToCommentDto(commentRepository.save(CommentMapper
                .mapNewCommentRequestToComment(newCommentRequest, user.getName(), item)));
    }

}
