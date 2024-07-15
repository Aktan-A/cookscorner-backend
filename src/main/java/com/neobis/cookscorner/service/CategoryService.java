package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.category.CategoryInDto;
import com.neobis.cookscorner.dto.category.CategoryOutDto;

import java.util.List;

public interface CategoryService {

    CategoryOutDto createCategory(CategoryInDto categoryInDto);
    List<CategoryOutDto> getAllCategories();

}
