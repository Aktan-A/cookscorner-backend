package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.recipe.RecipeInDto;
import com.neobis.cookscorner.dto.recipe.RecipeOutDto;

public interface RecipeService {

    RecipeOutDto createRecipe(RecipeInDto recipeInDto);

}
