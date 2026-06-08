package ru.practicum.shareit;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.item.dto.item.ItemDtoWithoutComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemInformation;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserInformation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    private ItemDtoWithoutComments itemDtoWithoutComments1;
    private ItemDtoWithoutComments itemDtoWithoutComments2;
    private final NewUserRequest newUserRequest1 = new NewUserRequest();
    private final NewUserRequest newUserRequest2 = new NewUserRequest();
    private UserDto userDto1;
    private UserDto userDto2;
    private final NewBookingRequest newBookingRequest = new NewBookingRequest();

    private BookingDto bookingDto;
    private final LocalDateTime startBooking = LocalDateTime.now().plusMinutes(5);
    private final LocalDateTime endBooking = startBooking.plusDays(20);

    @BeforeAll
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
        itemDtoWithoutComments1 = itemService.addNewItem(newItemRequest1, userDto1.getId());
        itemDtoWithoutComments2 = itemService.addNewItem(newItemRequest2, userDto2.getId());

        newBookingRequest.setItemId(itemDtoWithoutComments1.getId());
        newBookingRequest.setStart(startBooking);
        newBookingRequest.setEnd(endBooking);
        bookingDto = bookingService.addNewBooking(newBookingRequest, userDto2.getId());
        bookingService.changeBookingStatus(userDto1.getId(), bookingDto.getId(), true);
    }

    @Test
    public void testUserService() {
        Assertions.assertEquals(newUserRequest1.getName(),
                userService.getUser(userDto1.getId()).getName());
        Assertions.assertEquals(newUserRequest2.getName(),
                userService.getUser(userDto2.getId()).getName());

        UpdateUserInformation newUpdateUserInformation = new UpdateUserInformation();
        newUpdateUserInformation.setName("Nikita");

        userService.updateUser(newUpdateUserInformation, userDto2.getId());

        Assertions.assertEquals("Nikita", userService.getUser(userDto2.getId()).getName());
    }

    @Test
    public void testItemService() {
        Assertions.assertEquals(newItemRequest1.getName(),
                itemService.getItem(itemDtoWithoutComments1.getId()).getName());
        Assertions.assertEquals(newItemRequest2.getName(),
                itemService.getItem(itemDtoWithoutComments2.getId()).getName());
        Assertions.assertThrows(NotFoundItemException.class, () ->
                itemService.getItem(9999L));

        Assertions.assertEquals(1, itemService.getUserItems(userDto1.getId()).size());

        UpdateItemInformation updateItemInformation = new UpdateItemInformation();
        updateItemInformation.setName("Case");
        itemService.updateItemInformation(updateItemInformation, userDto2.getId(), itemDtoWithoutComments2.getId());

        Assertions.assertEquals("Case", itemService.getUserItems(userDto2.getId()).getFirst().getName());
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
        Assertions.assertEquals(1, bookingService.getBookingsByUserId(userDto2.getId(), "ALL").size());

        BookingDto bookingDto1 = bookingService.getBookingById(bookingDto.getId(), userDto2.getId());

        Assertions.assertEquals(BookingStatus.APPROVED, bookingDto1.getStatus());
    }

}
