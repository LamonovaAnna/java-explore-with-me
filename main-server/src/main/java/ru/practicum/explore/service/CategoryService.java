package ru.practicum.explore.service;

import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.model.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId) throws ObjectNotFoundException;

    CategoryDto createCategory(NewCategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(CategoryDto categoryDto);
}
