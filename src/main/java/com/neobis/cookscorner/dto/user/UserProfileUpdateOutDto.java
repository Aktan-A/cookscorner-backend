package com.neobis.cookscorner.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateOutDto {

    private Long id;
    private String name;
    private String bio;

    private String profileImageUrl;

}
