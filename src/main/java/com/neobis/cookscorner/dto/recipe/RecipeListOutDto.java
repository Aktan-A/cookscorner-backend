package com.neobis.cookscorner.dto.recipe;

import com.neobis.cookscorner.dto.category.CategoryOutDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeListOutDto {

    private Long id;
    private String name;
    private Integer preparationTime;
    private CategoryOutDto category;

    private String imageUrl;
    private String authorName;
    private Integer likesAmount;
    private Integer savesAmount;

}
