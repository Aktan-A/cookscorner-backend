package com.neobis.cookscorner.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequestDto {

    @NotEmpty(message = "Refresh token is required")
    private String refreshToken;

}
