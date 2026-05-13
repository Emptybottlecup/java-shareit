package ru.practicum.shareit.request.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RequestRepositoryImpl implements RequestRepository {

    private final Map<Long, ItemRequest> itemRequests;
    private Long currentId = 0L;

    public ItemRequest addNewItemRequest(ItemRequest itemRequest) {

        itemRequest.setId(generateNewId());
        itemRequest.setCreated(LocalDate.now());
        itemRequests.put(itemRequest.getId(), itemRequest);
        log.info("Новый запрос на вещь с описанием = {} добавлен", itemRequest.getDescription());
        return itemRequest;
    }

    public List<ItemRequest> getAllItemRequests() {
        return itemRequests.values()
                .stream()
                .toList();
    }

    public List<ItemRequest> getItemRequestsByUserId(Long userId) {
        return itemRequests.values()
                .stream()
                .filter(item -> item.getRequestor().equals(userId))
                .toList();
    }

    private Long generateNewId() {
        return ++currentId;
    }

}
