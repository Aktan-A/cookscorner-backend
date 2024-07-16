package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.user.UserListOutDto;
import com.neobis.cookscorner.model.User;
import com.neobis.cookscorner.repository.UserRepository;
import com.neobis.cookscorner.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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

}
