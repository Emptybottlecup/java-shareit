package ru.practicum.shareit.request.dao;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository {

    ItemRequest addNewItemRequest(ItemRequest itemRequest);

    List<ItemRequest> getAllItemRequests();

    List<ItemRequest> getItemRequestsByUserId(Long userId);

}
