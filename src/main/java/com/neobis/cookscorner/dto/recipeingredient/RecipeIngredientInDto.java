package com.neobis.cookscorner.dto.recipeingredient;

import com.neobis.cookscorner.enums.IngredientMeasureType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientInDto {

    @NotEmpty(message = "Ingredient name is required")
    private String name;

    @NotEmpty(message = "Ingredient quantity is required")
    private Double quantity;

    @NotEmpty(message = "Ingredient measure type is required")
    private IngredientMeasureType measure;

}
