package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item addNewItem(Item item);

    List<Item> getUserItems(Long userId);

    Optional<Item> getItem(Long itemId);

    List<Item> searchItem(String searchText);

}
