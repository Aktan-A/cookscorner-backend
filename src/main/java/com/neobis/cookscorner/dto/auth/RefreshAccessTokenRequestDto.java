package com.neobis.cookscorner.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshAccessTokenRequestDto {

    private String refreshToken;

}
