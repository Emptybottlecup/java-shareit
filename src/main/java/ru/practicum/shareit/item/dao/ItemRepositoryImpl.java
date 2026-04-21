package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items;
    private Long currentId = 0L;

    public Item addNewItem(Item item) {
        item.setId(generateNewId());
        items.put(item.getId(), item);
        return item;
    }

    public Optional<Item> getItem(Long itemId) {
        if (!items.containsKey(itemId)) {
            return Optional.empty();
        }
        return Optional.of(items.get(itemId));
    }

    public List<Item> getUserItems(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner(), userId))
                .toList();
    }

    public List<Item> searchItem(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return new ArrayList<>();
        }

        return items.values()
                .stream()
                .filter(item -> (item.getDescription().toLowerCase().contains(searchText.toLowerCase())
                        || item.getName().toLowerCase().contains(searchText.toLowerCase()))
                        && item.getAvailable())
                .toList();
    }

    private Long generateNewId() {
        return ++currentId;
    }

}
