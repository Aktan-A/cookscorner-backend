package com.neobis.cookscorner.repository;

import com.neobis.cookscorner.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void existsByName_shouldReturnTrue() {
        String categoryName = "Test category";
        Category category = new Category(categoryName);
        categoryRepository.save(category);

        Boolean exists = categoryRepository.existsByName(categoryName);

        assertTrue(exists);
    }

    @Test
    void existsByName_shouldReturnFalse() {
        String categoryName = "Test category";

        Boolean exists = categoryRepository.existsByName(categoryName);

        assertFalse(exists);
    }
}