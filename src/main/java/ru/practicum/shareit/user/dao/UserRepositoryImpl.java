package ru.practicum.shareit.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users;
    private final Set<String> emails;
    private Long currentId = 0L;

    public User addNewUser(User user) {
        user.setId(generateNewId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    public Optional<User> getUser(Long itemId) {
        if (!users.containsKey(itemId)) {
            return Optional.empty();
        }
        return Optional.of(users.get(itemId));
    }

    public Boolean emailIsExist(String email) {
        return emails.contains(email);
    }

    public void deleteUser(Long userId) {
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    private Long generateNewId() {
        return ++currentId;
    }

}
