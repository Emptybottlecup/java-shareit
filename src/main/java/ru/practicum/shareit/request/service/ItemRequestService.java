package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addNewItemRequest(NewItemRequestRequest newItemRequestRequest, Long userId);

    List<ItemRequestDto> getAllItemRequests();

    List<ItemRequestDto> getItemRequestsByUserId(Long userId);

}
