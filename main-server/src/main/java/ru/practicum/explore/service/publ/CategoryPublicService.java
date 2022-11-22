package ru.practicum.explore.service.publ;

import ru.practicum.explore.model.category.CategoryDto;

import java.util.List;

public interface CategoryPublicService {

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long categoryId);

}
