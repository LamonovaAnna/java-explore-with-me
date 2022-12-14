package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByNameContainingIgnoreCase(String name);

    @Query("SELECT u FROM User u WHERE u.id IN ?1")
    List<User> getAllUsersByIds(List<Long> ids, Pageable pageable);

}
