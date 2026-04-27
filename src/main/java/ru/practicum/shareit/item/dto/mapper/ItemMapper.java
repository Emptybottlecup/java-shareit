package ru.practicum.shareit.item.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemInformation;
import ru.practicum.shareit.item.model.Item;

@UtilityClass
public class ItemMapper {

    public static ItemDto mapItemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();

        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setId(item.getId());
        itemDto.setOwner(item.getOwner());

        return itemDto;
    }

    public static Item mapNewItemRequestToItem(NewItemRequest newItemRequest, Long userId) {
        Item item = new Item();

        item.setName(newItemRequest.getName());
        item.setDescription(newItemRequest.getDescription());
        item.setAvailable(newItemRequest.getAvailable());
        item.setOwner(userId);

        return item;
    }

    public static Item mapUpdateItemInformationToItem(UpdateItemInformation updateItemInformation, Item item) {
        if (updateItemInformation.hasName()) {
            item.setName(updateItemInformation.getName());
        }
        if (updateItemInformation.hasDescription()) {
            item.setDescription(updateItemInformation.getDescription());
        }
        if (updateItemInformation.hasAvailable()) {
            item.setAvailable(updateItemInformation.getAvailable());
        }

        return item;
    }

}
