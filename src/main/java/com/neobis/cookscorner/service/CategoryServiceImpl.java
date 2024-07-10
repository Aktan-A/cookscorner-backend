package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.cateogory.CategoryInDto;
import com.neobis.cookscorner.dto.cateogory.CategoryOutDto;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.model.Category;
import com.neobis.cookscorner.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

}
