package ru.practicum.explore.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.exception.ObjectParameterConflictException;
import ru.practicum.explore.mapper.UserMapper;
import ru.practicum.explore.model.user.NewUserRequest;
import ru.practicum.explore.model.user.User;
import ru.practicum.explore.model.user.UserDto;
import ru.practicum.explore.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl implements UserAdminService {
    private final UserRepository userRepository;

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        checkUserName(newUserRequest.getName());
        log.info("User {} created", newUserRequest);
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Override
    public void deleteUserById(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.info("User with id {} not found", userId);
            throw new ObjectNotFoundException(String.format("User with id %d not found", userId));
        }
        log.info("User with id {} was deleted", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getUsers(List<Long> userIds, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id"));
        if (userIds == null || userIds.isEmpty()) {
            log.info("Return all users");
            return UserMapper.toUserDtos(userRepository.findAll(pageable)
                    .stream()
                    .collect(Collectors.toList()));
        }
        log.info("Return users with ids {}", userIds);
        return UserMapper.toUserDtos(userRepository.getAllUsersByIds(userIds, pageable));
    }

    private void checkUserName(String name) {
        User user = userRepository.findByNameContainingIgnoreCase(name);
        if (user != null) {
            log.error("User with name - {} already exists", name);
            throw new ObjectParameterConflictException(String.format("User with name: %s already exists", name));
        }
    }
}
