package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users;
    private final Set<String> emails;
    private Long currentId = 0L;

    public User addNewUser(User user) {
        user.setId(generateNewId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.info("Новый пользователь с именем = {} добавлен", user.getName());
        return user;
    }

    public Optional<User> getUser(Long userId) {
        if (!users.containsKey(userId)) {
            log.info("Пользователя с id = {} не существует", userId);
            return Optional.empty();
        }
        return Optional.of(users.get(userId));
    }

    public Boolean emailIsExist(String email) {
        return emails.contains(email);
    }

    public void deleteUser(Long userId) {
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
        log.info("Пользователь с id = {} удален", userId);
    }

    private Long generateNewId() {
        return ++currentId;
    }

    public void deleteAll() {
        currentId = 0L;
        users.clear();
        emails.clear();
    }

}
