package com.neobis.cookscorner.service;

import com.neobis.cookscorner.dto.auth.*;
import com.neobis.cookscorner.enums.UserRole;
import com.neobis.cookscorner.exception.ResourceExistsException;
import com.neobis.cookscorner.exception.ResourceNotFoundException;
import com.neobis.cookscorner.model.RefreshToken;
import com.neobis.cookscorner.model.User;
import com.neobis.cookscorner.repository.UserRepository;
import com.neobis.cookscorner.security.JwtService;
import com.neobis.cookscorner.security.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private RefreshToken refreshToken;
    private RegisterRequestDto registerRequestDto;
    private RegisterResponseDto registerResponseDto;
    private LoginRequestDto loginRequestDto;
    private LoginResponseDto loginResponseDto;
    private RefreshAccessTokenRequestDto refreshAccessTokenRequestDto;
    private RefreshAccessTokenResponseDto refreshAccessTokenResponseDto;

    @BeforeEach
    void setUp() {
        user = new User(
                "john.doe@gmail.com",
                "John Doe",
                "testpassword",
                UserRole.ROLE_USER,
                "test bio"
        );
        refreshToken = new RefreshToken(
                "test-refresh",
                LocalDateTime.now(),
                user
        );
        user.setId(1L);
        registerRequestDto = new RegisterRequestDto(
                "John Doe",
                "john.doe@gmail.com",
                "testpassword"
        );
        registerResponseDto = new RegisterResponseDto(
                1L,
                "John Doe",
                "john.doe@gmail.com"
        );
        loginRequestDto = new LoginRequestDto(
                "john.doe@gmail.com",
                "testpassword"
        );
        loginResponseDto = new LoginResponseDto(
                "test-access",
                "test-refresh"
        );
        refreshAccessTokenRequestDto = new RefreshAccessTokenRequestDto(
                "test-refresh"
        );
        refreshAccessTokenResponseDto = new RefreshAccessTokenResponseDto(
                "test-access"
        );
    }

    @Test
    void register_ShouldReturnRegisterResponseDto() {
        when(userRepository.existsByEmail(loginRequestDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequestDto.getPassword())).thenReturn("testpassword");
        when(userRepository.save(any())).thenReturn(user);
        when(modelMapper.map(user, RegisterResponseDto.class)).thenReturn(registerResponseDto);

        RegisterResponseDto result = authService.register(registerRequestDto);

        verify(userRepository).existsByEmail(registerRequestDto.getEmail());
        verify(passwordEncoder).encode(registerRequestDto.getPassword());
        verify(userRepository).save(any());
        verify(modelMapper).map(user, RegisterResponseDto.class);
    }

    @Test
    void register_ShouldThrowResourceExistsException() {
        when(userRepository.existsByEmail(loginRequestDto.getEmail())).thenReturn(true);

        assertThrows(ResourceExistsException.class, () -> authService.register(registerRequestDto));
    }

    @Test
    void login_ShouldReturnLoginResponseDto() {
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.ofNullable(user));
        when(jwtService.generateToken(user)).thenReturn(loginResponseDto.getAccessToken());
        when(refreshTokenService.createRefreshToken(user)).thenReturn(refreshToken);

        LoginResponseDto result = authService.login(loginRequestDto);

        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail(loginRequestDto.getEmail());
        verify(jwtService).generateToken(user);
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void login_ShouldThrowResourceNotFoundException() {
        when(userRepository.findByEmail(loginRequestDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authService.login(loginRequestDto));
    }

    @Test
    void refreshAccessToken_ShouldReturnRefreshAccessTokenResponseDto() {
        when(refreshTokenService.validateRefreshTokenByToken(refreshAccessTokenRequestDto.getRefreshToken()))
                .thenReturn(refreshToken);
        when(jwtService.generateToken(refreshToken.getUser()))
                .thenReturn(refreshAccessTokenResponseDto.getAccessToken());

        RefreshAccessTokenResponseDto result = authService.refreshAccessToken(
                refreshAccessTokenRequestDto
        );

        verify(refreshTokenService).validateRefreshTokenByToken(
                refreshAccessTokenRequestDto.getRefreshToken()
        );
        verify(jwtService).generateToken(refreshToken.getUser());
    }
}