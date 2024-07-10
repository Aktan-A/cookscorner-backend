package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.cateogory.CategoryInDto;
import com.neobis.cookscorner.dto.cateogory.CategoryOutDto;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.model.Category;
import com.neobis.cookscorner.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryOutDto createCategory(CategoryInDto categoryInDto) {
        Boolean categoryExists = categoryRepository.existsByName(categoryInDto.getName());
        if (categoryExists) {
            throw new ResourceExistsException(
                    String.format("Category with name %s already exists.", categoryInDto.getName()));
        }

        Category category = new Category(categoryInDto.getName());
        return modelMapper.map(categoryRepository.save(category), CategoryOutDto.class);
    }

    @Override
    public List<CategoryOutDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> modelMapper.map(category, CategoryOutDto.class))
                .collect(Collectors.toList());
    }

}
