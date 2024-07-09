package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.LoginRequestDto;
import com.neobis.cookscorner.dto.LoginResponseDto;
import com.neobis.cookscorner.dto.RegisterRequestDto;
import com.neobis.cookscorner.dto.RegisterResponseDto;

public interface AuthService {

    RegisterResponseDto register(RegisterRequestDto registerRequestDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

}
