package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.cateogory.CategoryInDto;
import com.neobis.cookscorner.dto.cateogory.CategoryOutDto;

import java.util.List;

public interface CategoryService {

    CategoryOutDto createCategory(CategoryInDto categoryInDto);
    List<CategoryOutDto> getAllCategories();

}
