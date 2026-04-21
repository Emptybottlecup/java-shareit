package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewItemRequestRequest;
import ru.practicum.shareit.request.dto.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final RequestRepository requestsRep;
    private final UserService userService;

    public ItemRequestDto addNewItemRequest(NewItemRequestRequest newItemRequestRequest, Long userId) {
        userService.getUser(userId);
        ItemRequest itemRequest = ItemRequestMapper.mapNewItemRequestRequestToItemRequest(newItemRequestRequest,
                userId);
        return ItemRequestMapper.mapItemRequestToItemRequestDto(requestsRep.addNewItemRequest(itemRequest));
    }

    public List<ItemRequestDto> getAllItemRequests() {
        return requestsRep.getAllItemRequests()
                .stream()
                .map(ItemRequestMapper::mapItemRequestToItemRequestDto)
                .toList();
    }

    public List<ItemRequestDto> getItemRequestsByUserId(Long userId) {
        userService.getUser(userId);
        return requestsRep.getItemRequestsByUserId(userId)
                .stream()
                .map(ItemRequestMapper::mapItemRequestToItemRequestDto)
                .toList();
    }

}
