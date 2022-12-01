package ru.practicum.explore.ewm.mapper;

import ru.practicum.explore.ewm.dto.NewUserRequestDto;
import ru.practicum.explore.ewm.dto.UserDto;
import ru.practicum.explore.ewm.dto.UserShortDto;
import ru.practicum.explore.ewm.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        if (user != null) {
            return UserDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .build();
        } else {
            return null;
        }
    }

    public static UserShortDto toUserShortDto(User user) {
        if (user != null) {
            return UserShortDto.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .build();
        } else {
            return null;
        }
    }

    public static User toUser(NewUserRequestDto userDto) {
        if (userDto != null) {
            User user = new User();
            user.setName(userDto.getName());
            user.setEmail(userDto.getEmail());
            return user;
        } else {
            return null;
        }
    }
}
