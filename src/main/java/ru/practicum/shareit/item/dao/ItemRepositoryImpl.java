package ru.practicum.shareit.item.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items;
    private Long currentId = 0L;

    public Item addNewItem(Item item) {
        item.setId(generateNewId());
        items.put(item.getId(), item);
        log.info("Добавлен новый item с именем = {}", item.getName());
        return item;
    }

    public Optional<Item> getItem(Long itemId) {
        if (!items.containsKey(itemId)) {
            log.info("Предмет с id = {} не найден", itemId);
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
            log.info("Предмет с описанием = {} не найден", searchText);
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

    public void deleteAll() {
        currentId = 0L;
        items.clear();
    }

}
