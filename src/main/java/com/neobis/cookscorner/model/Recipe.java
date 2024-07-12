package com.neobis.cookscorner.model;

import com.neobis.cookscorner.enums.RecipeDifficulty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"author", "image"})
public class Recipe extends BaseEntity {

    @Column(name = "preparation_time", nullable = false)
    private Integer preparationTime;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecipeDifficulty difficulty;

    @OneToOne
    @JoinColumn(name = "author_user_id", nullable = false)
    private User author;

    @OneToOne
    @JoinColumn(name = "image_id", nullable = false)
    private Image image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recipe_categories",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "category_id"})
    )
    private Set<Category> categories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "saved_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
    )
    private Set<User> savedByUsers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "liked_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"recipe_id", "user_id"})
    )
    private Set<User> likedByUsers;

    @OneToMany(mappedBy = "recipe")
    private Set<RecipeIngredient> ingredients;

    public Recipe(
            Integer preparationTime, String description, RecipeDifficulty difficulty, User author, Image image,
            Set<Category> categories, Set<RecipeIngredient> ingredients) {
        this.preparationTime = preparationTime;
        this.description = description;
        this.difficulty = difficulty;
        this.author = author;
        this.image = image;
        this.categories = categories;
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(preparationTime, recipe.preparationTime) && Objects.equals(description, recipe.description) && difficulty == recipe.difficulty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(preparationTime, description, difficulty);
    }
}
