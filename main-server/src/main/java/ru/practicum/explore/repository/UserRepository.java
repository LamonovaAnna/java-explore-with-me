package ru.practicum.explore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByNameContainingIgnoreCase(String name);

    @Query("select u from User u where :userIds is null or u.id in (:userIds)")
    Page<User> getAllByIds(@Param("userIds") List<Long> userIds, Pageable pageable);

}
