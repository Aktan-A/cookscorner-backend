package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.recipe.RecipeDetailsDto;
import com.neobis.cookscorner.dto.recipe.RecipeInDto;
import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.recipe.RecipeOutDto;
import com.neobis.cookscorner.dto.recipeingredient.RecipeIngredientInDto;
import com.neobis.cookscorner.exception.InvalidRequestException;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.exception.ResourceNotFoundException;
import com.neobis.cookscorner.model.*;
import com.neobis.cookscorner.repository.CategoryRepository;
import com.neobis.cookscorner.repository.ImageRepository;
import com.neobis.cookscorner.repository.RecipeRepository;
import com.neobis.cookscorner.specification.RecipeSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public RecipeOutDto createRecipe(RecipeInDto recipeInDto) {
        Boolean nameExists = recipeRepository.existsByName(recipeInDto.getName());

        if (nameExists) {
            throw new ResourceExistsException(
                    String.format("Recipe with name %s already exists.", recipeInDto.getName())
            );
        }

        ModelMapper localMapper = new ModelMapper();
        localMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        localMapper.createTypeMap(RecipeInDto.class, Recipe.class)
                .addMappings(mapper -> {
                    mapper.skip(Recipe::setIngredients);
                });

        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Image> image = imageRepository.findById(recipeInDto.getImageId());
        if (image.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Image with id %s was not found.", recipeInDto.getImageId()));
        }
        Image imageModel = image.get();

        Optional<Category> category = categoryRepository.findById(recipeInDto.getCategoryId());
        if (category.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Category with id %s was not found.", recipeInDto.getCategoryId()));
        }
        Category categoryModel = category.get();

        Recipe recipe = localMapper.map(recipeInDto, Recipe.class);
        recipe.setAuthor(author);
        recipe.setImage(imageModel);
        recipe.setCategory(categoryModel);

        for (RecipeIngredientInDto ingredientDto : recipeInDto.getIngredients()) {
            RecipeIngredient ingredient = modelMapper.map(ingredientDto, RecipeIngredient.class);
            recipe.addRecipeIngredient(ingredient);
        }

        recipe = recipeRepository.save(recipe);
        RecipeOutDto recipeOutDto = modelMapper.map(recipe, RecipeOutDto.class);
        recipeOutDto.setImageUrl(recipe.getImage().getImageUrl());
        return recipeOutDto;
    }

    @Override
    public Page<RecipeListOutDto> getRecipes(Long categoryId, String searchTerm, Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAll(
                RecipeSpecification.filterByCategoryAndSearch(categoryId, searchTerm), pageable);
        return recipes.map(recipe -> {
                    RecipeListOutDto dto = modelMapper.map(recipe, RecipeListOutDto.class);
                    dto.setImageUrl(recipe.getImage().getImageUrl());
                    dto.setAuthorName(recipe.getAuthor().getName());
                    dto.setLikesAmount(recipe.getLikedByUsers().size());
                    dto.setSavesAmount(recipe.getSavedByUsers().size());
                    return dto;
                });
    }

    @Override
    public RecipeDetailsDto getRecipeById(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Recipe with id %s was not found.", recipeId)
            );
        }
        Recipe recipeModel = recipe.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        RecipeDetailsDto dto = modelMapper.map(recipeModel, RecipeDetailsDto.class);
        dto.setImageUrl(recipeModel.getImage().getImageUrl());
        dto.setLikesAmount(recipeModel.getLikedByUsers().size());
        dto.setSavesAmount(recipeModel.getSavedByUsers().size());
        dto.setIsLikedByUser(recipeModel.getLikedByUsers().contains(user));
        dto.setIsSavedByUser(recipeModel.getSavedByUsers().contains(user));
        return dto;
    }

    @Override
    public String likeOrUnlikeRecipeById(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Recipe with id %s was not found.", recipeId)
            );
        }
        Recipe recipeModel = recipe.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String resultText;
        if (recipeModel.getLikedByUsers().contains(user)) {
            recipeModel.getLikedByUsers().remove(user);
            resultText = "Recipe successfully unliked.";
        } else {
            recipeModel.getLikedByUsers().add(user);
            resultText = "Recipe successfully liked.";
        }
        recipeRepository.save(recipeModel);
        return resultText;
    }

    @Override
    public String saveOrUnsaveRecipeById(Long recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Recipe with id %s was not found.", recipeId)
            );
        }
        Recipe recipeModel = recipe.get();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String resultText;
        if (recipeModel.getSavedByUsers().contains(user)) {
            recipeModel.getSavedByUsers().remove(user);
            resultText = "Recipe successfully unsaved.";
        } else {
            recipeModel.getSavedByUsers().add(user);
            resultText = "Recipe successfully saved.";
        }
        recipeRepository.save(recipeModel);
        return resultText;
    }

}
