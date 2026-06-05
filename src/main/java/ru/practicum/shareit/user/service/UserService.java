package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserInformation;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto addNewUser(NewUserRequest newUserRequest);

    UserDto getUser(Long userId);

    UserDto updateUser(UpdateUserInformation updateUserInformation, Long userId);

    void deleteUser(Long userId);

}
