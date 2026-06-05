package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.item.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.item.ItemDtoWithoutComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemInformation;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    public List<ItemDtoWithComments> getUserItems(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.getUserItems(userId);
    }

    @PostMapping
    public ItemDtoWithoutComments addNewItem(@RequestBody @Valid NewItemRequest newItemRequest,
                                             @RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.addNewItem(newItemRequest, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithComments getItem(@PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoWithoutComments updateItemInformation(@RequestBody @Valid UpdateItemInformation updateItemInformation,
                                                        @RequestHeader(HEADER_USER_ID) Long userId,
                                                        @PathVariable Long itemId) {
        return itemService.updateItemInformation(updateItemInformation, userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDtoWithoutComments> searchItem(@RequestParam("text") String searchText) {
        return itemService.searchItem(searchText);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody NewCommentRequest newCommentRequest,
                                 @RequestHeader(HEADER_USER_ID) Long userId,
                                 @PathVariable Long itemId) {
        return itemService.addComment(newCommentRequest, userId, itemId);
    }

}
