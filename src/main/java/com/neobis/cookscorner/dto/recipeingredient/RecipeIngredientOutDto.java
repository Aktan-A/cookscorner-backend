package com.neobis.cookscorner.dto.recipeingredient;

import com.neobis.cookscorner.enums.IngredientMeasureType;
import com.neobis.cookscorner.util.IngredientTextUtil;
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
    private String quantityText;

    public String getQuantityText() {
        return IngredientTextUtil.getIngredientQuantityText(quantity, measure);
    }

}
