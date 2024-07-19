package com.neobis.cookscorner.dto.user;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileUpdateInDto {

    @Size(max = 300)
    private String name;

    @Size(max = 300)
    private String bio;

    private Long profileImageId;

}
