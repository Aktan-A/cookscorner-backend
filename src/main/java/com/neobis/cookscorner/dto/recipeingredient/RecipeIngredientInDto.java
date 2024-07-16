package com.neobis.cookscorner.dto.recipeingredient;

import com.neobis.cookscorner.enums.IngredientMeasureType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientInDto {

    @NotEmpty(message = "Ingredient name is required")
    @Size(max = 300, message = "Name must not be longer than 300 characters")
    private String name;

    @NotEmpty(message = "Ingredient quantity is required")
    @Positive(message = "Quantity must be positive")
    private Double quantity;

    @NotEmpty(message = "Ingredient measure type is required")
    private IngredientMeasureType measure;

}
