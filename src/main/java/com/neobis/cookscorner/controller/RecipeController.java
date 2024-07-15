package com.neobis.cookscorner.controller;

import com.neobis.cookscorner.dto.recipe.RecipeInDto;
import com.neobis.cookscorner.dto.recipe.RecipeOutDto;
import com.neobis.cookscorner.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @Operation(summary = "Create a new recipe", description = "Creates a new recipe and returns the details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully created"),
            @ApiResponse(responseCode = "404", description = "Image or category was not found")
    })
    @PostMapping
    public ResponseEntity<RecipeOutDto> createRecipe(@RequestBody @Valid RecipeInDto recipeInDto) {
        return ResponseEntity.ok(recipeService.createRecipe(recipeInDto));
    }

}
