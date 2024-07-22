package com.neobis.cookscorner.repository;

import com.neobis.cookscorner.enums.RecipeDifficulty;
import com.neobis.cookscorner.enums.UserRole;
import com.neobis.cookscorner.model.Category;
import com.neobis.cookscorner.model.Image;
import com.neobis.cookscorner.model.Recipe;
import com.neobis.cookscorner.model.User;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RecipeRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private User user;
    private Image image;
    private Category category;
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
        userRepository.save(user);

        image = new Image("http://test.com", "test-id");
        imageRepository.save(image);

        category = new Category("Test category");
        categoryRepository.save(category);

        recipe = new Recipe(
                "Test recipe",
                30,
                "Test description",
                RecipeDifficulty.MEDIUM,
                user,
                image,
                category,
                new HashSet<>()
        );
        recipeRepository.save(recipe);

    }

    @AfterEach
    void tearDown() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();
        imageRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void findSavedRecipesByUserId_ShouldReturnPageRecipe() {
        recipe.getSavedByUsers().add(user);

        Pageable pageable = Pageable.ofSize(5);
        Page<Recipe> result = recipeRepository.findSavedRecipesByUserId(user.getId(), pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(recipe.getId(), result.stream().findFirst().get().getId());
    }

    @Test
    void findSavedRecipesByUserId_ShouldReturnEmptyPageRecipe() {
        Pageable pageable = Pageable.ofSize(5);
        Page<Recipe> result = recipeRepository.findSavedRecipesByUserId(user.getId()+1L, pageable);

        assertEquals(0, result.getTotalElements());
    }
}