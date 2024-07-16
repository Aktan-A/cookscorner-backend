package com.neobis.cookscorner.service;

import static org.junit.jupiter.api.Assertions.*;

import com.neobis.cookscorner.dto.category.CategoryInDto;
import com.neobis.cookscorner.dto.category.CategoryOutDto;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.model.Category;
import com.neobis.cookscorner.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryInDto categoryInDto;
    private Category category;
    private CategoryOutDto categoryOutDto;

    @BeforeEach
    public void setUp() {
        categoryInDto = new CategoryInDto("Test category");
        category = new Category("Test category");
        categoryOutDto = new CategoryOutDto(1L, "Test category");
    }

    @Test
    void createCategory_ShouldReturnCategoryOutDto() {
        when(categoryRepository.existsByName(categoryInDto.getName())).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(category);
        when(modelMapper.map(category, CategoryOutDto.class)).thenReturn(categoryOutDto);

        CategoryOutDto result = categoryService.createCategory(categoryInDto);

        verify(categoryRepository).existsByName(categoryInDto.getName());
        verify(categoryRepository).save(category);
        assertEquals(categoryOutDto.getId(), result.getId());
    }
    @Test
    void createCategory_ShouldThrowResourceExistsException() {
        when(categoryRepository.existsByName(categoryInDto.getName())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> categoryService.createCategory(categoryInDto));
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        List<Category> categories = List.of(category);
        when(categoryRepository.findAll()).thenReturn(categories);
        when(modelMapper.map(category, CategoryOutDto.class)).thenReturn(categoryOutDto);

        List<CategoryOutDto> result = categoryService.getAllCategories();

        verify(categoryRepository).findAll();
        assertEquals(1, result.size());
        assertEquals(category.getName(), result.getFirst().getName());
    }

}