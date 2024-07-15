package com.neobis.cookscorner.repository;

import com.neobis.cookscorner.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Boolean existsByName(String name);

}
