package com.neobis.cookscorner.controller;

import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.user.UserListOutDto;
import com.neobis.cookscorner.dto.user.UserProfileOutDto;
import com.neobis.cookscorner.dto.user.UserProfileUpdateInDto;
import com.neobis.cookscorner.dto.user.UserProfileUpdateOutDto;
import com.neobis.cookscorner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get a filtered list of users",
            description = "Returns a filtered list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<Page<UserListOutDto>> getUsers(
            @RequestParam(required = false) String searchTerm,
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUsers(searchTerm, pageable));
    }

    @Operation(
            summary = "Get logged-in user profile",
            description = "Returns logged in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "User was not found")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileOutDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @Operation(
            summary = "Get logged-in user recipes",
            description = "Returns a list of logged-in user recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User recipes successfully retrieved")
    })
    @GetMapping("/me/recipes")
    public ResponseEntity<Page<RecipeListOutDto>> getCurrentUserRecipes(
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getCurrentUserRecipes(pageable));
    }

    @Operation(
            summary = "Get logged-in user saved recipes",
            description = "Returns a list of logged-in user saved recipes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User saved recipes successfully retrieved")
    })
    @GetMapping("/me/recipes/saved")
    public ResponseEntity<Page<RecipeListOutDto>> getCurrentUserSavedRecipes(
            Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getCurrentUserSavedRecipes(pageable));
    }

    @Operation(
            summary = "Update user profile",
            description = "Returns updated user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile successfully updated"),
            @ApiResponse(responseCode = "404", description = "User or image was not found")
    })
    @PatchMapping("/me")
    public ResponseEntity<UserProfileUpdateOutDto> getCurrentUser(
            @RequestBody UserProfileUpdateInDto userProfileUpdateInDto) throws IOException {
        return ResponseEntity.ok(userService.updateUserProfile(userProfileUpdateInDto));
    }

}
