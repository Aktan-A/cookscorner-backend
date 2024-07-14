package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.auth.*;

public interface AuthService {

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    RefreshAccessTokenResponseDto refreshAccessToken(RefreshAccessTokenRequestDto refreshAccessTokenRequestDto);

    void logout(LogoutRequestDto logoutRequestDto);
}
