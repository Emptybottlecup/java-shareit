package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ExistedEmail;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserInformation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRep;

    public UserDto addNewUser(NewUserRequest newUserRequest) {
        try {
            User newUser = userRep.save(UserMapper.mapNewUserRequestToUser(newUserRequest));

            return UserMapper.mapUserToUserDto(newUser);
        } catch (Throwable th) {
            throw new ExistedEmail(String.format("User with email = %s already exist", newUserRequest.getEmail()));
        }
    }

    public UserDto getUser(Long userId) {
        User user = userRep.findById(userId).orElseThrow(() ->
                new NotFoundUserException(String.format("User with id = %d not exist", userId)));

        return UserMapper.mapUserToUserDto(user);
    }

    public UserDto updateUser(UpdateUserInformation updateUserInformation, Long userId) {
        try {
            User user = userRep.findById(userId).orElseThrow(() ->
                    new NotFoundUserException(String.format("User with id = %d not found", userId)));

            User updatedUser = UserMapper.mapUpdatedUserInformationToUser(updateUserInformation, user);

            userRep.save(updatedUser);

            return UserMapper.mapUserToUserDto(updatedUser);
        } catch (Throwable th) {
            throw new ExistedEmail(String.format("User with email = %s already exist",
                    updateUserInformation.getEmail()));
        }
    }

    public void deleteUser(Long userId) {
        try {
            userRep.deleteById(userId);
        } catch (Throwable th) {
            throw new NotFoundUserException(String.format("User with id = %d not found", userId));
        }
    }

}
