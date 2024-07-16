package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.recipe.RecipeInDto;
import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.recipe.RecipeOutDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {

    RecipeOutDto createRecipe(RecipeInDto recipeInDto);

    Page<RecipeListOutDto> getRecipes(Long categoryId, String searchTerm, Pageable pageable);

    RecipeOutDto getRecipeById(Long recipeId);

    void likeRecipeById(Long recipeId);

    void unlikeRecipeById(Long recipeId);
}
