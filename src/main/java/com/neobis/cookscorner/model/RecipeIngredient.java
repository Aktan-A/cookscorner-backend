package com.neobis.cookscorner.model;

import com.neobis.cookscorner.enums.IngredientMeasureType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Entity
@Table(name = "recipe_ingredients")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"recipe"})
public class RecipeIngredient extends BaseEntity {

    @Column(nullable = false)
    private Double quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngredientMeasureType measure;

    @ManyToOne
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    public RecipeIngredient(Double quantity, IngredientMeasureType measure, Recipe recipe) {
        this.quantity = quantity;
        this.measure = measure;
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredient that = (RecipeIngredient) o;
        return Objects.equals(quantity, that.quantity) && measure == that.measure;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, measure);
    }
}
