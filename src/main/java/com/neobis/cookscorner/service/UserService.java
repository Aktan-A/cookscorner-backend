package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.user.UserListOutDto;
import com.neobis.cookscorner.dto.user.UserProfileOutDto;
import com.neobis.cookscorner.dto.user.UserProfileUpdateInDto;
import com.neobis.cookscorner.dto.user.UserProfileUpdateOutDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface UserService {

    Page<UserListOutDto> getUsers(String searchTerm, Pageable pageable);

    UserProfileOutDto getCurrentUserProfile();

    Page<RecipeListOutDto> getCurrentUserRecipes(Pageable pageable);

    Page<RecipeListOutDto> getCurrentUserSavedRecipes(Pageable pageable);

    UserProfileUpdateOutDto updateUserProfile(UserProfileUpdateInDto userProfileUpdateInDto) throws IOException;

    UserProfileOutDto getUserProfileById(Long id);

}
