package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.recipe.RecipeListOutDto;
import com.neobis.cookscorner.dto.user.UserListOutDto;
import com.neobis.cookscorner.dto.user.UserProfileOutDto;
import com.neobis.cookscorner.dto.user.UserProfileUpdateInDto;
import com.neobis.cookscorner.dto.user.UserProfileUpdateOutDto;
import com.neobis.cookscorner.exception.ResourceNotFoundException;
import com.neobis.cookscorner.model.Image;
import com.neobis.cookscorner.model.Recipe;
import com.neobis.cookscorner.model.User;
import com.neobis.cookscorner.repository.ImageRepository;
import com.neobis.cookscorner.repository.RecipeRepository;
import com.neobis.cookscorner.repository.UserRepository;
import com.neobis.cookscorner.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RecipeRepository recipeRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Override
    public Page<UserListOutDto> getUsers(String searchTerm, Pageable pageable) {
        Page<User> users = userRepository.findAll(
                UserSpecification.filterBySearchTerm(searchTerm), pageable);
        return users.map(user -> {
            UserListOutDto dto = new UserListOutDto();
            dto.setName(user.getName());
            if (user.getProfileImage() != null) {
                dto.setProfileImageUrl(user.getProfileImage().getImageUrl());
            }
            return dto;
        });
    }

    @Override
    @Transactional
    public UserProfileOutDto getCurrentUserProfile() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findById(currentUser.getId());

        if (user.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("User with id %s was not found.", currentUser.getId())
            );
        }

        User userModel = user.get();
        UserProfileOutDto dto = modelMapper.map(userModel, UserProfileOutDto.class);
        if (userModel.getProfileImage() != null) {
            dto.setProfileImageUrl(userModel.getProfileImage().getImageUrl());
        }
        dto.setRecipeCount(userModel.getRecipes().size());
        dto.setFollowerCount(userModel.getFollowers().size());
        dto.setFollowingCount(userModel.getFollowing().size());
        return dto;
    }

    @Override
    @Transactional
    public Page<RecipeListOutDto> getCurrentUserRecipes(Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Recipe> recipes = recipeRepository.findAllByAuthor(currentUser, pageable);
        return recipes.map(
                recipe -> {
                    RecipeListOutDto dto = modelMapper.map(recipe, RecipeListOutDto.class);
                    dto.setImageUrl(recipe.getImage().getImageUrl());
                    dto.setAuthorName(recipe.getAuthor().getName());
                    dto.setLikesAmount(recipe.getLikedByUsers().size());
                    dto.setSavesAmount(recipe.getSavedByUsers().size());
                    return dto;
                }
        );
    }

    @Override
    @Transactional
    public Page<RecipeListOutDto> getCurrentUserSavedRecipes(Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Recipe> recipes = recipeRepository.findSavedRecipesByUserId(currentUser.getId(), pageable);
        return recipes.map(
                recipe -> {
                    RecipeListOutDto dto = modelMapper.map(recipe, RecipeListOutDto.class);
                    dto.setImageUrl(recipe.getImage().getImageUrl());
                    dto.setAuthorName(recipe.getAuthor().getName());
                    dto.setLikesAmount(recipe.getLikedByUsers().size());
                    dto.setSavesAmount(recipe.getSavedByUsers().size());
                    return dto;
                }
        );
    }

    @Override
    @Transactional
    public UserProfileUpdateOutDto updateUserProfile(
            UserProfileUpdateInDto userProfileUpdateInDto) throws IOException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findById(currentUser.getId());

        if (user.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("User with id %s was not found.", currentUser.getId())
            );
        }

        User userModel = user.get();

        if (userProfileUpdateInDto.getName() != null) {
            userModel.setName(userProfileUpdateInDto.getName());
        }

        if (userProfileUpdateInDto.getProfileImageId() != null) {
            Optional<Image> image = imageRepository.findById(userProfileUpdateInDto.getProfileImageId());
            if (image.isEmpty()) {
                throw new ResourceNotFoundException(
                        String.format("Image with id %s was not found.", userProfileUpdateInDto.getProfileImageId())
                );
            }

            if (userModel.getProfileImage() != null) {
                imageService.deleteImageById(userModel.getProfileImage().getId());
            }

            userModel.setProfileImage(image.get());
        }

        if (userProfileUpdateInDto.getBio() != null) {
            userModel.setBio(userProfileUpdateInDto.getBio());
        }

        UserProfileUpdateOutDto dto = modelMapper.map(userRepository.save(userModel), UserProfileUpdateOutDto.class);
        if (userModel.getProfileImage() != null) {
            dto.setProfileImageUrl(userModel.getProfileImage().getImageUrl());
        }
        return dto;
    }

}
