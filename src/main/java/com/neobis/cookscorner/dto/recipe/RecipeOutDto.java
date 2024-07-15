package com.neobis.cookscorner.dto.recipe;

import com.neobis.cookscorner.dto.category.CategoryOutDto;
import com.neobis.cookscorner.dto.image.ImageOutDto;
import com.neobis.cookscorner.dto.recipeingredient.RecipeIngredientOutDto;
import com.neobis.cookscorner.enums.RecipeDifficulty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeOutDto {

    private Long id;
    private String name;
    private Integer preparationTime;
    private String description;
    private RecipeDifficulty difficulty;
    private String imageUrl;
    private CategoryOutDto category;
    private List<RecipeIngredientOutDto> ingredients;
    private LocalDateTime createdAt;

}
