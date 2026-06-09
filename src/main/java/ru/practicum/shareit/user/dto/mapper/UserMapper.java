package ru.practicum.shareit.user.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UpdateUserInformation;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

@UtilityClass
public class UserMapper {

    public UserDto mapUserToUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());

        return userDto;
    }

    public User mapNewUserRequestToUser(NewUserRequest newUserRequest) {
        User user = new User();

        user.setEmail(newUserRequest.getEmail());
        user.setName(newUserRequest.getName());

        return user;
    }

    public User mapUpdatedUserInformationToUser(UpdateUserInformation updateUserInformation, User user) {
        if (updateUserInformation.hasEmail()) {
            user.setEmail(updateUserInformation.getEmail());
        }
        if (updateUserInformation.hasName()) {
            user.setName(updateUserInformation.getName());
        }

        return user;
    }

}
