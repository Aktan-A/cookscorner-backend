package com.neobis.cookscorner.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListOutDto {

    private String name;
    private String profileImageUrl;

}
