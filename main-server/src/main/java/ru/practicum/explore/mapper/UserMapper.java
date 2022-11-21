package ru.practicum.explore.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.user.NewUserRequest;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.user.UserDto;
import ru.practicum.explore.model.user.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static User toUserFromUserShortDto(UserShortDto userShortDto) {
        User user = new User();
        user.setName(userShortDto.getName());
        user.setEmail(null);
        return user;
    }

    public static User toUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

    public static List<UserDto> toUserDtos(List<User> users) {
        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }
}
