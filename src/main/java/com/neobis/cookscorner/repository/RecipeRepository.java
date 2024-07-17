package com.neobis.cookscorner.repository;

import com.neobis.cookscorner.model.Recipe;
import com.neobis.cookscorner.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    Boolean existsByName(String name);

    Page<Recipe> findAllByAuthor(User author, Pageable pageable);

}
