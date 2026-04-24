package ru.practicum.shareit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ItemIsNotAvailable;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemInformation;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserInformation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDate;

@SpringBootTest
class ShareItTests {

    @Autowired
    private UserService userService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private BookingService bookingService;

    private final NewItemRequest newItemRequest1 = new NewItemRequest();
    private final NewItemRequest newItemRequest2 = new NewItemRequest();
    private ItemDto itemDto1;
    private ItemDto itemDto2;

    private final NewUserRequest newUserRequest1 = new NewUserRequest();
    private final NewUserRequest newUserRequest2 = new NewUserRequest();
    private UserDto userDto1;
    private UserDto userDto2;

    @BeforeEach
    public void contextLoad() {
        newUserRequest1.setEmail("dobr@mail.ru");
        newUserRequest1.setName("Andrey");
        newUserRequest2.setEmail("dobe20@mail.ru");
        newUserRequest2.setName("Anton");

        userDto1 = userService.addNewUser(newUserRequest1);
        userDto2 = userService.addNewUser(newUserRequest2);

        newItemRequest1.setName("Hammer");
        newItemRequest1.setDescription("Good hammer");
        newItemRequest1.setAvailable(true);
        newItemRequest2.setName("Saw");
        newItemRequest2.setDescription("Good saw");
        newItemRequest2.setAvailable(true);

        itemDto1 = itemService.addNewItem(newItemRequest1, userDto1.getId());
        itemDto2 = itemService.addNewItem(newItemRequest2, userDto2.getId());
    }

    @AfterEach
    public void clearContext() {
        userService.deleteAll();
        itemService.deleteAll();
    }

    @Test
    public void testUserService() {
        Assertions.assertEquals(newUserRequest1.getName(),
                userService.getUser(userDto1.getId()).getName());
        Assertions.assertEquals(newUserRequest2.getName(),
                userService.getUser(userDto2.getId()).getName());

        userService.deleteUser(userDto1.getId());
        Assertions.assertThrows(NotFoundUserException.class, () ->
                userService.getUser(userDto1.getId()));

        UpdateUserInformation newUpdateUserInformation = new UpdateUserInformation();
        newUpdateUserInformation.setName("Nikita");

        userService.updateUser(newUpdateUserInformation, userDto2.getId());

        Assertions.assertEquals("Nikita", userService.getUser(userDto2.getId()).getName());
    }

    @Test
    public void testItemService() {
        Assertions.assertEquals(newItemRequest1.getName(),
                itemService.getItem(itemDto1.getId()).getName());
        Assertions.assertEquals(newItemRequest2.getName(),
                itemService.getItem(itemDto2.getId()).getName());
        Assertions.assertThrows(NotFoundItemException.class, () ->
                itemService.getItem(9999L));

        Assertions.assertEquals(1, itemService.getUserItems(userDto1.getId()).size());

        UpdateItemInformation updateItemInformation = new UpdateItemInformation();
        updateItemInformation.setName("Case");
        itemService.updateItemInformation(updateItemInformation, userDto1.getId(), itemDto1.getId());

        Assertions.assertEquals("Case", itemService.getUserItems(userDto1.getId()).getFirst().getName());
        Assertions.assertEquals(1, itemService.searchItem("Cas").size());
    }

    @Test
    public void testItemRequestService() {
        NewItemRequestRequest newItemRequestRequest = new NewItemRequestRequest();
        newItemRequestRequest.setDescription("Need PC");
        itemRequestService.addNewItemRequest(newItemRequestRequest, userDto1.getId());

        Assertions.assertEquals(1, itemRequestService.getAllItemRequests().size());
        Assertions.assertEquals(1, itemRequestService.getItemRequestsByUserId(userDto1.getId()).size());
    }

    @Test
    public void testBookingService() {
        NewBookingRequest newBookingRequest = new NewBookingRequest();

        LocalDate startTime = LocalDate.now();
        LocalDate endTime = startTime.plusYears(2);

        newBookingRequest.setStartBooking(startTime);
        newBookingRequest.setEndBooking(endTime);

        bookingService.addNewBooking(newBookingRequest, userDto1.getId(), itemDto2.getId());

        Assertions.assertEquals(1, bookingService.getBookingsByItemId(itemDto2.getId()).size());
        Assertions.assertEquals(1, bookingService.getBookingsByUserId(userDto1.getId()).size());
        Assertions.assertThrows(ItemIsNotAvailable.class, () -> bookingService.addNewBooking(newBookingRequest,
                userDto2.getId(), itemDto2.getId()));
    }

}
