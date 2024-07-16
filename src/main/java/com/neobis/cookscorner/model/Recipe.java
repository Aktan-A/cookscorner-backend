package com.neobis.cookscorner.model;

import com.neobis.cookscorner.enums.RecipeDifficulty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"author", "image"})
public class Recipe extends BaseEntity {

    @Column(nullable = false, unique = true, length = 300)
    private String name;

    @Column(name = "preparation_time", nullable = false)
    private Integer preparationTime;

    @Lob
    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeDifficulty difficulty;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_user_id")
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "image_id")
    private Image image;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "saved_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
    )
    private Set<User> savedByUsers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "liked_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
    )
    private Set<User> likedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST)
    private Set<RecipeIngredient> ingredients = new HashSet<>();

    public Recipe(
            String name, Integer preparationTime, String description, RecipeDifficulty difficulty, User author,
            Image image, Category category, Set<RecipeIngredient> ingredients) {
        this.name = name;
        this.preparationTime = preparationTime;
        this.description = description;
        this.difficulty = difficulty;
        this.author = author;
        this.image = image;
        this.category = category;
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(name, recipe.name) && Objects.equals(preparationTime, recipe.preparationTime) && Objects.equals(description, recipe.description) && difficulty == recipe.difficulty && Objects.equals(author, recipe.author) && Objects.equals(image, recipe.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, preparationTime, description, difficulty, author, image);
    }

    public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
        ingredients.add(recipeIngredient);
        recipeIngredient.setRecipe(this);
    }

}
