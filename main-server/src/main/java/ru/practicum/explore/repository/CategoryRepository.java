package ru.practicum.explore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.model.category.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllCategories(Pageable pageable);

    Category findByNameContainingIgnoreCase(String name);
}
