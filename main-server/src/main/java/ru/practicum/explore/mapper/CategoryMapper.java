package ru.practicum.explore.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.model.category.NewCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public static Category toCategoryFromNew(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }

    public static Category toUpdateCategory(Category category, Category updateCategory) {
        category.setName(updateCategory.getName() == null ? category.getName() : updateCategory.getName());
        return category;
    }


    public static List<CategoryDto> toCategoryDtos(List<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).collect(Collectors.toList());
    }
}