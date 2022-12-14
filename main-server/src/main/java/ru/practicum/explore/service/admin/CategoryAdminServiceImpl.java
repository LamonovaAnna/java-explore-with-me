package ru.practicum.explore.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.exception.ObjectParameterConflictException;
import ru.practicum.explore.mapper.CategoryMapper;
import ru.practicum.explore.model.category.Category;
import ru.practicum.explore.model.category.CategoryDto;
import ru.practicum.explore.model.category.NewCategoryDto;
import ru.practicum.explore.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(NewCategoryDto newCategoryDto) {
        checkCategoryName(newCategoryDto.getName());
        log.info("Category {} was created", newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(CategoryMapper.toCategoryFromNew(newCategoryDto)));
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        checkCategoryName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(
                CategoryMapper.toUpdateCategory(categoryRepository.findById(categoryDto.getId())
                                .orElseThrow(() -> new ObjectNotFoundException(
                                        String.format("Category with id %d not found", categoryDto.getId()))),
                        CategoryMapper.toCategory(categoryDto))));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        log.info("Category with id = {} was deleted", categoryId);
        categoryRepository.deleteById(categoryId);
    }

    private void checkCategoryName(String name) {
        Category category = categoryRepository.findByNameContainingIgnoreCase(name);
        if (category != null) {
            log.error("Category with name {} already exists", name);
            throw new ObjectParameterConflictException(String.format("Category with name %s already exists", name));
        }
    }
}