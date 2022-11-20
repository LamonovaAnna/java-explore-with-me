package ru.practicum.explore.service;

import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.model.user.NewUserRequest;
import ru.practicum.explore.model.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUserById(Long userId) throws ObjectNotFoundException;

    List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size);
}
