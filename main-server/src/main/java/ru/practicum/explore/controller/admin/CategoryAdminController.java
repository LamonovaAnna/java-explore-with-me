package ru.practicum.explore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.model.category.NewCategoryDto;
import ru.practicum.explore.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryAdminController {
    private final CategoryService categoryService;


    @PostMapping
    public CategoryDto createCategory(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return categoryService.createCategory(newCategoryDto);
    }

    @PatchMapping
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}