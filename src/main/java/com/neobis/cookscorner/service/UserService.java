package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.user.UserListOutDto;
import com.neobis.cookscorner.dto.user.UserProfileOutDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserListOutDto> getUsers(String searchTerm, Pageable pageable);

    UserProfileOutDto getCurrentUserProfile();

    Page<RecipeListOutDto> getCurrentUserRecipes(Pageable pageable);

}
