package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.comment.NewCommentRequest;
import ru.practicum.shareit.item.dto.item.ItemDtoWithComments;
import ru.practicum.shareit.item.dto.item.ItemDtoWithoutComments;
import ru.practicum.shareit.item.dto.item.NewItemRequest;
import ru.practicum.shareit.item.dto.item.UpdateItemInformation;

import java.util.List;

public interface ItemService {

    ItemDtoWithoutComments addNewItem(NewItemRequest newItemRequest, Long userId);

    List<ItemDtoWithComments> getUserItems(Long userId);

    ItemDtoWithComments getItem(Long itemId);

    ItemDtoWithoutComments updateItemInformation(UpdateItemInformation updateItemInformation, Long userId, Long itemId);

    List<ItemDtoWithoutComments> searchItem(String searchText);

    CommentDto addComment(NewCommentRequest newCommentRequest, Long userId, Long itemId);

}
