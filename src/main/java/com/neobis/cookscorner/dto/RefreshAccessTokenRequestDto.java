package com.neobis.cookscorner.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshAccessTokenRequestDto {

    private String refreshToken;

}
