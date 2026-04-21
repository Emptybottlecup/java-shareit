package ru.practicum.shareit.request.dto.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDate;

public class ItemRequestMapper {

    public static ItemRequest mapNewItemRequestRequestToItemRequest(NewItemRequestRequest newItemRequest, Long userId) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setCreated(LocalDate.now());
        itemRequest.setRequestor(userId);
        itemRequest.setDescription(newItemRequest.getDescription());

        return itemRequest;
    }

    public static ItemRequestDto mapItemRequestToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setRequestor(itemRequest.getRequestor());

        return itemRequestDto;
    }

}
