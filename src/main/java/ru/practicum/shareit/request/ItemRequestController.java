package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final static String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemRequestService itemRequestService;

    @GetMapping
    public List<ItemRequestDto> getAllItemRequests() {
        return itemRequestService.getAllItemRequests();
    }

    @GetMapping("/{userId}")
    public List<ItemRequestDto> getItemRequests(@PathVariable Long userId) {
        return itemRequestService.getItemRequestsByUserId(userId);
    }

    @PostMapping
    public ItemRequestDto addNewItemRequest(@RequestBody @Valid NewItemRequestRequest newItemRequestRequest,
                                            @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemRequestService.addNewItemRequest(newItemRequestRequest, userId);
    }

}
