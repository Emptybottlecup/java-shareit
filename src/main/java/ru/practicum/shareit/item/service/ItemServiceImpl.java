package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemInformation;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRep;
    private final UserService userService;

    public ItemDto addNewItem(NewItemRequest newItemRequest, Long userId) {
        userService.getUser(userId);

        Item newItem = ItemMapper.mapNewItemRequestToItem(newItemRequest, userId);

        return ItemMapper.mapItemToItemDto(itemRep.addNewItem(newItem));
    }

    public List<ItemDto> getUserItems(Long userId) {
        userService.getUser(userId);
        return itemRep
                .getUserItems(userId)
                .stream()
                .map(ItemMapper::mapItemToItemDto)
                .toList();
    }

    public ItemDto getItem(Long itemId) {
        Item item = itemRep.getItem(itemId).orElseThrow(
                () -> new NotFoundItemException(String.format("Item with id = %d not found", itemId)));
        return ItemMapper.mapItemToItemDto(item);
    }

    public ItemDto updateItemInformation(UpdateItemInformation updateItemInformation, Long userId, Long itemId) {
        Optional<Item> item = itemRep.getItem(itemId);

        if (item.isEmpty()) {
            throw new NotFoundItemException(String.format("Item with id = %d not found", itemId));
        }
        if (!item.get().getOwner().equals(userId)) {
            log.info("Пользователь с id = {} не является владельцем item с id = {}", userId, itemId);
            throw new AccessException(String.format("This user id = %d dont have item id = %d", userId, itemId));
        }

        Item updatedItem = ItemMapper.mapUpdateItemInformationToItem(updateItemInformation, item.get());

        return ItemMapper.mapItemToItemDto(updatedItem);
    }

    public List<ItemDto> searchItem(String searchText) {
        return itemRep.searchItem(searchText)
                .stream()
                .map(ItemMapper::mapItemToItemDto)
                .toList();
    }

    public void deleteAll() {
        itemRep.deleteAll();
    }

}
