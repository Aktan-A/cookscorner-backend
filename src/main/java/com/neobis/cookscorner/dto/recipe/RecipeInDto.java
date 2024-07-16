package com.neobis.cookscorner.dto.recipe;

import com.neobis.cookscorner.dto.recipeingredient.RecipeIngredientInDto;
import com.neobis.cookscorner.enums.RecipeDifficulty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeInDto {

    @NotEmpty(message = "Name is required")
    @Size(max = 300, message = "Name must not be longer than 300 characters")
    private String name;

    @NotNull(message = "Preparation time is required")
    private Integer preparationTime;

    @NotEmpty(message = "Description is required")
    private String description;

    @NotNull(message = "Difficulty is required")
    private RecipeDifficulty difficulty;

    @NotNull(message = "Image id is required")
    private Long imageId;

    @NotNull(message = "Category id is required")
    private Long categoryId;

    @NotNull(message = "Ingredients are required")
    private List<RecipeIngredientInDto> ingredients;

}
