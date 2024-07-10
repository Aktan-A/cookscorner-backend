package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.*;

public interface AuthService {

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    RefreshAccessTokenResponseDto refreshAccessToken(RefreshAccessTokenRequestDto refreshAccessTokenRequestDto);
}
