package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ExistedEmail;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserInformation;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRep;

    public UserDto addNewUser(NewUserRequest newUserRequest) {
        if (userRep.emailIsExist(newUserRequest.getEmail())) {
            throw new ExistedEmail(String.format("Такой email = %s уже существует", newUserRequest.getEmail()));
        }

        User newUser = UserMapper.mapNewUserRequestToUser(newUserRequest);

        return UserMapper.mapUserToUserDto(userRep.addNewUser(newUser));
    }

    public UserDto getUser(Long userId) {
        if (userRep.getUser(userId).isEmpty()) {
            throw new NotFoundUserException(String.format("User with id = %d not found", userId));
        }

        return UserMapper.mapUserToUserDto(userRep.getUser(userId).get());
    }

    public UserDto updateUser(UpdateUserInformation updateUserInformation, Long userId) {
        Optional<User> user = userRep.getUser(userId);

        if (user.isEmpty()) {
            throw new NotFoundUserException(String.format("User with id = %d not found", userId));
        }
        if (updateUserInformation.hasEmail() && userRep.emailIsExist(updateUserInformation.getEmail())) {
            throw new ExistedEmail(String.format("Такой email = %s уже существует", updateUserInformation
                    .getEmail()));
        }

        User updatedUser = UserMapper.mapUpdatedUserInformationToUser(updateUserInformation, user.get());

        return UserMapper.mapUserToUserDto(updatedUser);
    }

    public void deleteUser(Long userId) {
        Optional<User> user = userRep.getUser(userId);
        if (user.isEmpty()) {
            throw new NotFoundUserException(String.format("User with id = %d not found", userId));
        }
        userRep.deleteUser(userId);
    }

}
