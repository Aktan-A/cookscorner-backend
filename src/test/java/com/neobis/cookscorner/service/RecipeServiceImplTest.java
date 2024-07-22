package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.category.CategoryOutDto;
import com.neobis.cookscorner.dto.recipe.RecipeDetailsDto;
import com.neobis.cookscorner.dto.recipe.RecipeInDto;
import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.recipe.RecipeOutDto;
import com.neobis.cookscorner.dto.recipeingredient.RecipeIngredientInDto;
import com.neobis.cookscorner.dto.recipeingredient.RecipeIngredientOutDto;
import com.neobis.cookscorner.dto.user.UserOutDto;
import com.neobis.cookscorner.enums.IngredientMeasureType;
import com.neobis.cookscorner.enums.RecipeDifficulty;
import com.neobis.cookscorner.enums.UserRole;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.exception.ResourceNotFoundException;
import com.neobis.cookscorner.model.*;
import com.neobis.cookscorner.repository.CategoryRepository;
import com.neobis.cookscorner.repository.ImageRepository;
import com.neobis.cookscorner.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private User user;
    private UserOutDto userOutDto;
    private Image image;
    private Category category;
    private CategoryOutDto categoryOutDto;
    private RecipeIngredient recipeIngredient;
    private RecipeIngredientOutDto recipeIngredientOutDto;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        user = new User(
                "test@gmail.com",
                "Test User",
                "123",
                UserRole.ROLE_USER,
                "Test bio"
        );
        userOutDto = new UserOutDto(1L, "Test User");

        image = new Image("http://test.com", "test-id");

        category = new Category("Test category");
        categoryOutDto = new CategoryOutDto(1L, "Test category");

        HashSet<RecipeIngredient> ingredients = new HashSet<>();
        recipeIngredient = new RecipeIngredient(
                "Test ingredient",
                10.0,
                IngredientMeasureType.CUP,
                recipe);
        recipeIngredientOutDto = new RecipeIngredientOutDto(
                "Test ingredient",
                10.0,
                IngredientMeasureType.CUP,
                "10 cup"
        );
        ingredients.add(recipeIngredient);
        recipe = new Recipe(
                "Test recipe",
                30,
                "Test description",
                RecipeDifficulty.MEDIUM,
                user,
                image,
                category,
                ingredients);
    }

    @Test
    void createRecipe_ShouldReturnRecipeOutDto() {
        RecipeIngredientInDto ingredientInDto = new RecipeIngredientInDto("Test ingredient",
                10.0,
                IngredientMeasureType.CUP);
        List<RecipeIngredientInDto> ingredientsIn = List.of(ingredientInDto);
        RecipeInDto recipeInDto = new RecipeInDto(
                recipe.getName(),
                recipe.getPreparationTime(),
                recipe.getDescription(),
                recipe.getDifficulty(),
                recipe.getImage().getId(),
                recipe.getCategory().getId(),
                ingredientsIn
        );
        when(recipeRepository.existsByName(recipeInDto.getName())).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(user);
        when(imageRepository.findById(recipeInDto.getImageId())).thenReturn(Optional.of(image));
        when(categoryRepository.findById(recipeInDto.getCategoryId())).thenReturn(Optional.of(category));
        when(modelMapper.map(ingredientInDto, RecipeIngredient.class)).thenReturn(recipeIngredient);

        RecipeOutDto recipeOutDto = new RecipeOutDto(
                1L,
                recipeInDto.getName(),
                recipeInDto.getPreparationTime(),
                recipeInDto.getDescription(),
                recipeInDto.getDifficulty(),
                categoryOutDto,
                List.of(recipeIngredientOutDto),
                LocalDateTime.now(),
                image.getImageUrl(),
                user.getName(),
                1,1
        );
        when(recipeRepository.save(any())).thenReturn(recipe);
        when(modelMapper.map(recipe, RecipeOutDto.class)).thenReturn(recipeOutDto);

        RecipeOutDto result = recipeService.createRecipe(recipeInDto);

        verify(recipeRepository).existsByName(recipeInDto.getName());
        verify(userService).getCurrentUser();
        verify(imageRepository).findById(recipeInDto.getImageId());
        verify(categoryRepository).findById(recipeInDto.getCategoryId());
        verify(recipeRepository).save(any());
    }

    @Test
    void createRecipe_IfNameExists_ShouldThrowResourceExistsException() {
        RecipeIngredientInDto ingredientInDto = new RecipeIngredientInDto("Test ingredient",
                10.0,
                IngredientMeasureType.CUP);
        List<RecipeIngredientInDto> ingredientsIn = List.of(ingredientInDto);
        RecipeInDto recipeInDto = new RecipeInDto(
                recipe.getName(),
                recipe.getPreparationTime(),
                recipe.getDescription(),
                recipe.getDifficulty(),
                recipe.getImage().getId(),
                recipe.getCategory().getId(),
                ingredientsIn
        );
        when(recipeRepository.existsByName(recipeInDto.getName())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> recipeService.createRecipe(recipeInDto));
    }

    @Test
    void createRecipe_IfImageNotFound_ShouldThrowResourceNotFoundException() {
        RecipeIngredientInDto ingredientInDto = new RecipeIngredientInDto("Test ingredient",
                10.0,
                IngredientMeasureType.CUP);
        List<RecipeIngredientInDto> ingredientsIn = List.of(ingredientInDto);
        RecipeInDto recipeInDto = new RecipeInDto(
                recipe.getName(),
                recipe.getPreparationTime(),
                recipe.getDescription(),
                recipe.getDifficulty(),
                recipe.getImage().getId(),
                recipe.getCategory().getId(),
                ingredientsIn
        );
        when(recipeRepository.existsByName(recipeInDto.getName())).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(user);
        when(imageRepository.findById(recipeInDto.getImageId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.createRecipe(recipeInDto));
    }

    @Test
    void createRecipe_IfCategoryNotFound_ShouldThrowResourceNotFoundException() {
        RecipeIngredientInDto ingredientInDto = new RecipeIngredientInDto("Test ingredient",
                10.0,
                IngredientMeasureType.CUP);
        List<RecipeIngredientInDto> ingredientsIn = List.of(ingredientInDto);
        RecipeInDto recipeInDto = new RecipeInDto(
                recipe.getName(),
                recipe.getPreparationTime(),
                recipe.getDescription(),
                recipe.getDifficulty(),
                recipe.getImage().getId(),
                recipe.getCategory().getId(),
                ingredientsIn
        );
        when(recipeRepository.existsByName(recipeInDto.getName())).thenReturn(false);
        when(userService.getCurrentUser()).thenReturn(user);
        when(imageRepository.findById(recipeInDto.getImageId())).thenReturn(Optional.of(image));
        when(categoryRepository.findById(recipeInDto.getCategoryId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.createRecipe(recipeInDto));
    }

    @Test
    void getRecipes_ShouldReturnPaginatedRecipeListOutDto() {
        List<Recipe> recipeList = Collections.singletonList(recipe);
        Page<Recipe> recipePage = new PageImpl<>(recipeList);
        when(recipeRepository.findAll((Specification<Recipe>) any(), any(Pageable.class))).thenReturn(recipePage);
        RecipeListOutDto recipeListOutDto = new RecipeListOutDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getPreparationTime(),
                categoryOutDto,
                image.getImageUrl(),
                user.getName(),
                1, 1
        );
        when(modelMapper.map(recipe, RecipeListOutDto.class)).thenReturn(recipeListOutDto);

        Pageable pageable= PageRequest.of(0, 10);
        Page<RecipeListOutDto> result = recipeService.getRecipes(1L, "test", pageable);

        verify(recipeRepository).findAll((Specification<Recipe>) any(), any(Pageable.class));
    }

    @Test
    void getRecipeById_ShouldReturnRecipeDetailsDto() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        RecipeDetailsDto recipeDetailsDto = new RecipeDetailsDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getPreparationTime(),
                recipe.getDescription(),
                recipe.getDifficulty(),
                userOutDto,
                categoryOutDto,
                List.of(recipeIngredientOutDto),
                LocalDateTime.now(),
                image.getImageUrl(),
                1, 1, false, false
        );
        when(modelMapper.map(recipe, RecipeDetailsDto.class)).thenReturn(recipeDetailsDto);

        RecipeDetailsDto result = recipeService.getRecipeById(1L);

        assertEquals(recipe.getId(), result.getId());
        verify(recipeRepository).findById(1L);
    }

    @Test
    void getRecipeById_ShouldThrowResourceNotFoundException() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.getRecipeById(1L));
    }

    @Test
    void likeOrUnlikeRecipeById_ShouldReturnLikedMessage() {
        HashSet<User> likedByUsers = new HashSet<>();
        recipe.setLikedByUsers(likedByUsers);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userService.getCurrentUser()).thenReturn(user);
        when(recipeRepository.save(any())).thenReturn(recipe);

        String result = recipeService.likeOrUnlikeRecipeById(1L);

        assertEquals("Recipe successfully liked.", result);
        verify(recipeRepository).findById(1L);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void likeOrUnlikeRecipeById_ShouldReturnUnlikedMessage() {
        HashSet<User> likedByUsers = new HashSet<>();
        likedByUsers.add(user);
        recipe.setLikedByUsers(likedByUsers);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userService.getCurrentUser()).thenReturn(user);
        when(recipeRepository.save(any())).thenReturn(recipe);

        String result = recipeService.likeOrUnlikeRecipeById(1L);

        assertEquals("Recipe successfully unliked.", result);
        verify(recipeRepository).findById(1L);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void likeOrUnlikeRecipeById_ShouldThrowResourceNotFoundException() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.likeOrUnlikeRecipeById(1L));
    }

    @Test
    void saveOrUnsaveRecipeById_ShouldReturnSavedMessage() {
        HashSet<User> savedByUsers = new HashSet<>();
        recipe.setSavedByUsers(savedByUsers);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userService.getCurrentUser()).thenReturn(user);
        when(recipeRepository.save(any())).thenReturn(recipe);

        String result = recipeService.saveOrUnsaveRecipeById(1L);

        assertEquals("Recipe successfully saved.", result);
        verify(recipeRepository).findById(1L);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void saveOrUnsaveRecipeById_ShouldReturnUnsavedMessage() {
        HashSet<User> savedByUsers = new HashSet<>();
        savedByUsers.add(user);
        recipe.setSavedByUsers(savedByUsers);
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(userService.getCurrentUser()).thenReturn(user);
        when(recipeRepository.save(any())).thenReturn(recipe);

        String result = recipeService.saveOrUnsaveRecipeById(1L);

        assertEquals("Recipe successfully unsaved.", result);
        verify(recipeRepository).findById(1L);
        verify(recipeRepository).save(recipe);
    }

    @Test
    void saveOrUnsaveRecipeById_ShouldThrowResourceNotFoundException() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> recipeService.saveOrUnsaveRecipeById(1L));
    }
}