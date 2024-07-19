package com.neobis.cookscorner.controller;

import com.neobis.cookscorner.dto.recipe.RecipeDetailsDto;
import com.neobis.cookscorner.dto.recipe.RecipeInDto;
import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.recipe.RecipeOutDto;
import com.neobis.cookscorner.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(
            summary = "Get a filtered list of recipes",
            description = "Returns a filtered list of recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<Page<RecipeListOutDto>> getRecipes(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String searchTerm,
            Pageable pageable
    ) {
        return ResponseEntity.ok(recipeService.getRecipes(categoryId, searchTerm, pageable));
    }

    @Operation(
            summary = "Get recipe details by id",
            description = "Returns a recipe details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe details successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Recipe with provided id was not found")
    })
    @GetMapping("/{recipeId}")
    public ResponseEntity<RecipeDetailsDto> getRecipe(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    @Operation(
            summary = "Like or unlike a recipe",
            description = "Likes a recipe or removes a like if it was already liked")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully liked or unliked"),
            @ApiResponse(responseCode = "404", description = "Recipe with provided id was not found")
    })
    @PostMapping("/{recipeId}/like")
    public ResponseEntity<String> likeRecipe(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity.ok(recipeService.likeOrUnlikeRecipeById(recipeId));
    }

    @Operation(
            summary = "Save or unsave a recipe",
            description = "Saves a recipe or unsaves it if it was already saved")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully saved or unsaved"),
            @ApiResponse(responseCode = "404", description = "Recipe with provided id was not found")
    })
    @PostMapping("/{recipeId}/save")
    public ResponseEntity<String> saveRecipe(@PathVariable("recipeId") Long recipeId) {
        return ResponseEntity.ok(recipeService.saveOrUnsaveRecipeById(recipeId));
    }

}
