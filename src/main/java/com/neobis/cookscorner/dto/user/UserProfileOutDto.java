package com.neobis.cookscorner.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileOutDto {

    private Long id;
    private String name;
    private String bio;

    private String profileImageUrl;
    private Integer recipeCount;
    private Integer followerCount;
    private Integer followingCount;
    private Boolean isFollowed;


}
