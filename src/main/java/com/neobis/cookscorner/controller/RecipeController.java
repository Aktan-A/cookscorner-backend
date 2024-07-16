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

    @Operation(summary = "Like a recipe", description = "Likes a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully liked"),
            @ApiResponse(responseCode = "404", description = "Recipe with provided id was not found")
    })
    @PostMapping("/{recipeId}/like")
    public ResponseEntity<String> likeRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.likeRecipeById(recipeId);
        return ResponseEntity.ok("Recipe successfully liked.");
    }

    @Operation(summary = "Unlike a recipe", description = "Removes like from the recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully unliked"),
            @ApiResponse(responseCode = "404", description = "Recipe with provided id was not found")
    })
    @PostMapping("/{recipeId}/unlike")
    public ResponseEntity<String> unlikeRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.unlikeRecipeById(recipeId);
        return ResponseEntity.ok("Recipe successfully unliked.");
    }

    @Operation(summary = "Save a recipe", description = "Saves a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully saved"),
            @ApiResponse(responseCode = "404", description = "Recipe with provided id was not found")
    })
    @PostMapping("/{recipeId}/save")
    public ResponseEntity<String> saveRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.saveRecipeById(recipeId);
        return ResponseEntity.ok("Recipe successfully saved.");
    }

    @Operation(summary = "Unsave a recipe", description = "Removes recipe from saved recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe successfully unsaved"),
            @ApiResponse(responseCode = "404", description = "Recipe with provided id was not found")
    })
    @PostMapping("/{recipeId}/unsave")
    public ResponseEntity<String> unsaveRecipe(@PathVariable("recipeId") Long recipeId) {
        recipeService.unsaveRecipeById(recipeId);
        return ResponseEntity.ok("Recipe successfully unsaved.");
    }

}
