package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.recipe.RecipeInDto;
import com.neobis.cookscorner.dto.recipe.RecipeOutDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {

    RecipeOutDto createRecipe(RecipeInDto recipeInDto);

    Page<RecipeOutDto> getRecipes(Long categoryId, String searchTerm, Pageable pageable);
}
