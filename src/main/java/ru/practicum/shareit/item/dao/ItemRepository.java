package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long userId);

    @Query("select i from Item i "
            + "where (LOWER(i.description) like LOWER(concat('%', :text, '%')) "
            + "or LOWER(i.name) like LOWER(concat('%', :text, '%'))) and i.available = true")
    List<Item> searchItemsByText(String text);

}
