package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemInformation;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getUserItems(userId);
    }

    @PostMapping
    public ItemDto addNewItem(@RequestBody @Valid NewItemRequest newItemRequest,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.addNewItem(newItemRequest, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemInformation(@RequestBody @Valid UpdateItemInformation updateItemInformation,
                                         @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long itemId) {
        return itemService.updateItemInformation(updateItemInformation, userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String searchText) {
        return itemService.searchItem(searchText);
    }

}
