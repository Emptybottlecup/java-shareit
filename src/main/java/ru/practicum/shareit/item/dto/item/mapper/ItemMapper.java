package ru.practicum.shareit.item.dto.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.item.ItemDtoWithoutComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemInformation;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@UtilityClass
public class ItemMapper {

    public static ItemDtoWithoutComments mapItemToItemDtoWithoutComments(Item item) {
        ItemDtoWithoutComments itemDtoWithoutComments = new ItemDtoWithoutComments();

        itemDtoWithoutComments.setName(item.getName());
        itemDtoWithoutComments.setDescription(item.getDescription());
        itemDtoWithoutComments.setAvailable(item.getAvailable());
        itemDtoWithoutComments.setId(item.getId());
        itemDtoWithoutComments.setOwner(item.getOwner().getId());

        return itemDtoWithoutComments;
    }

    public static ItemDtoWithComments mapItemToItemDtoWithComments(Item item, List<CommentDto> commentsDto) {
        ItemDtoWithComments itemDtoWithComments = new ItemDtoWithComments();

        itemDtoWithComments.setName(item.getName());
        itemDtoWithComments.setDescription(item.getDescription());
        itemDtoWithComments.setAvailable(item.getAvailable());
        itemDtoWithComments.setId(item.getId());
        itemDtoWithComments.setOwner(item.getOwner().getId());
        itemDtoWithComments.setComments(commentsDto);

        return itemDtoWithComments;
    }

    public static Item mapNewItemRequestToItem(NewItemRequest newItemRequest, User owner) {
        Item item = new Item();

        item.setName(newItemRequest.getName());
        item.setDescription(newItemRequest.getDescription());
        item.setAvailable(newItemRequest.getAvailable());
        item.setOwner(owner);

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
