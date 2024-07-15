package com.neobis.cookscorner.controller;

import com.neobis.cookscorner.dto.category.CategoryInDto;
import com.neobis.cookscorner.dto.category.CategoryOutDto;
import com.neobis.cookscorner.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category", description = "Creates a new category and returns the details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category successfully created"),
            @ApiResponse(responseCode = "400", description = "Category with this name already exists")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryOutDto> createCategory(@RequestBody @Valid CategoryInDto categoryInDto) {
        return ResponseEntity.ok(categoryService.createCategory(categoryInDto));
    }

    @Operation(summary = "Get all categories", description = "Returns a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<List<CategoryOutDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

}
