package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.category.CategoryOutDto;
import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.recipeingredient.RecipeIngredientOutDto;
import com.neobis.cookscorner.dto.user.UserListOutDto;
import com.neobis.cookscorner.dto.user.UserProfileOutDto;
import com.neobis.cookscorner.enums.IngredientMeasureType;
import com.neobis.cookscorner.enums.RecipeDifficulty;
import com.neobis.cookscorner.enums.UserRole;
import com.neobis.cookscorner.exception.ResourceNotFoundException;
import com.neobis.cookscorner.model.*;
import com.neobis.cookscorner.repository.ImageRepository;
import com.neobis.cookscorner.repository.RecipeRepository;
import com.neobis.cookscorner.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageService imageService;

    @InjectMocks
    @Spy
    private UserServiceImpl userService;

    private User user;
    private UserProfileOutDto userProfileOutDto;
    private Image image;
    private Category category;
    private CategoryOutDto categoryOutDto;
    private RecipeIngredient recipeIngredient;
    private Recipe recipe;
    private RecipeListOutDto recipeListOutDto;

    @BeforeEach
    void setUp() {
        user = new User(
                "test@gmail.com",
                "Test User",
                "123",
                UserRole.ROLE_USER,
                "Test bio"
        );
        user.setId(1L);
        image = new Image("http://test.com", "test-id");
        userProfileOutDto = new UserProfileOutDto(
                1L,
                "Test User",
                "Test bio",
                image.getImageUrl(),
                1, 1, 1, true
        );
        image = new Image("http://test.com", "test-id");

        category = new Category("Test category");
        categoryOutDto = new CategoryOutDto(1L, "Test category");
        HashSet<RecipeIngredient> ingredients = new HashSet<>();
        recipeIngredient = new RecipeIngredient(
                "Test ingredient",
                10.0,
                IngredientMeasureType.CUP,
                recipe);
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
        recipeListOutDto = new RecipeListOutDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getPreparationTime(),
                categoryOutDto,
                image.getImageUrl(),
                user.getName(),
                1, 1
        );
    }

    @Test
    void getUsers_ShouldReturnPaginatedUserListOutDto() {
        List<User> userList = Collections.singletonList(user);
        Page<User> userPage = new PageImpl<>(userList);
        when(userRepository.findAll((Specification<User>) any(), any(Pageable.class))).thenReturn(userPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<UserListOutDto> result = userService.getUsers("test", pageable);

        verify(userRepository).findAll((Specification<User>) any(), any(Pageable.class));
    }

    @Test
    void getCurrentUserProfile_ShouldReturnUserProfileOutDto() {
        doReturn(user).when(userService).getCurrentUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserProfileOutDto.class)).thenReturn(userProfileOutDto);

        UserProfileOutDto result = userService.getCurrentUserProfile();

        verify(userRepository).findById(1L);
    }

    @Test
    void getCurrentUserProfile_ShouldThrowResourceNotFoundException() {
        doReturn(user).when(userService).getCurrentUser();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getCurrentUserProfile());
    }

    @Test
    void getCurrentUserRecipes_ShouldReturnPaginatedRecipeListOutDto() {
        doReturn(user).when(userService).getCurrentUser();
        List<Recipe> recipeList = Collections.singletonList(recipe);
        Page<Recipe> recipePage = new PageImpl<>(recipeList);
        Pageable pageable = PageRequest.of(0, 10);
        when(recipeRepository.findAllByAuthor(user, pageable)).thenReturn(recipePage);
        when(modelMapper.map(recipe, RecipeListOutDto.class)).thenReturn(recipeListOutDto);

        Page<RecipeListOutDto> result = userService.getCurrentUserRecipes(pageable);

        verify(userService).getCurrentUser();
        verify(recipeRepository).findAllByAuthor(user, pageable);
    }

    @Test
    void getCurrentUserSavedRecipes_ShouldReturnPaginatedRecipeListOutDto() {
        doReturn(user).when(userService).getCurrentUser();
        List<Recipe> recipeList = Collections.singletonList(recipe);
        Page<Recipe> recipePage = new PageImpl<>(recipeList);
        Pageable pageable = PageRequest.of(0, 10);
        when(recipeRepository.findSavedRecipesByUserId(user.getId(), pageable)).thenReturn(recipePage);
        when(modelMapper.map(recipe, RecipeListOutDto.class)).thenReturn(recipeListOutDto);

        Page<RecipeListOutDto> result = userService.getCurrentUserSavedRecipes(pageable);

        verify(userService).getCurrentUser();
        verify(recipeRepository).findSavedRecipesByUserId(user.getId(), pageable);
    }

    @Test
    void getUserProfileById_ShouldReturnUserProfileOutDto() {
        doReturn(user).when(userService).getCurrentUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserProfileOutDto.class)).thenReturn(userProfileOutDto);

        UserProfileOutDto result = userService.getUserProfileById(user.getId());

        verify(userRepository).findById(user.getId());
    }

    @Test
    void getUserProfileById_ShouldThrowResourceNotFoundException() {
        doReturn(user).when(userService).getCurrentUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserProfileById(user.getId()));
    }

    @Test
    void followOrUnfollowUserById_ShouldReturnFollowedMessage() {
        doReturn(user).when(userService).getCurrentUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User followedUser = new User(
                "test1@gmail.com",
                "Test User 1",
                "123",
                UserRole.ROLE_USER,
                "Test bio"
        );
        followedUser.setId(2L);
        when(userRepository.findById(followedUser.getId())).thenReturn(Optional.of(followedUser));

        String result = userService.followOrUnfollowUserById(2L);

        assertEquals("User successfully followed.", result);
        verify(userRepository, times(2)).findById(anyLong());
    }

    @Test
    void followOrUnfollowUserById_IfUserNotFound_ShouldThrowResourceNotFoundException() {
        doReturn(user).when(userService).getCurrentUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.followOrUnfollowUserById(user.getId()));
    }

    @Test
    void followOrUnfollowUserById_IfFollowedUserNotFound_ShouldThrowResourceNotFoundException() {
        doReturn(user).when(userService).getCurrentUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.followOrUnfollowUserById(2L));
    }

}