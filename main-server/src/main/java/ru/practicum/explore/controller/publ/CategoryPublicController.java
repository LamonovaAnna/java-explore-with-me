package ru.practicum.explore.controller.publ;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") Integer from,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }
}