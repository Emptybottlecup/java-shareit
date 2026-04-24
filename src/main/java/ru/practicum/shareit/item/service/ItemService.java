package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemInformation;

import java.util.List;

public interface ItemService {

    ItemDto addNewItem(NewItemRequest newItemRequest, Long userId);

    List<ItemDto> getUserItems(Long userId);

    ItemDto getItem(Long itemId);

    ItemDto updateItemInformation(UpdateItemInformation updateItemInformation, Long userId, Long itemId);

    List<ItemDto> searchItem(String searchText);

    void deleteAll();

}
