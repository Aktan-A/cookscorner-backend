package com.neobis.cookscorner.dto.recipeingredient;

import com.neobis.cookscorner.enums.IngredientMeasureType;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeIngredientOutDto {

    private String name;
    private Double quantity;
    private IngredientMeasureType measure;

}
