package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> getUser(Long userId);

    User addNewUser(User user);

    Boolean emailIsExist(String email);

    void deleteUser(Long userId);

}
