package ru.practicum.shareit.request.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDate;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequest mapNewItemRequestRequestToItemRequest(NewItemRequestRequest newItemRequest, Long userId) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setCreated(LocalDate.now());
        itemRequest.setRequestor(userId);
        itemRequest.setDescription(newItemRequest.getDescription());

        return itemRequest;
    }

    public ItemRequestDto mapItemRequestToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setRequestor(itemRequest.getRequestor());

        return itemRequestDto;
    }

}
