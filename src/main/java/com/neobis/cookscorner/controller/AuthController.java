package com.neobis.cookscorner.controller;

import com.neobis.cookscorner.dto.auth.*;
import com.neobis.cookscorner.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register a new user", description = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "User with this email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.register(registerRequestDto));
    }

    @Operation(
            summary = "Authenticates a user",
            description = "Authenticates a user and returns an access token and a refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in"),
            @ApiResponse(responseCode = "404", description = "User with this email was not found")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @Operation(
            summary = "Refreshes an access token",
            description = "Validates the provided refresh token and returns a refreshed access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Access token successfully refreshed"),
            @ApiResponse(responseCode = "400", description = "Refresh token has expired"),
            @ApiResponse(responseCode = "404", description = "Refresh token was not found")
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshAccessTokenResponseDto> refreshToken(
            @RequestBody RefreshAccessTokenRequestDto refreshAccessTokenRequestDto) {
        return ResponseEntity.ok(authService.refreshAccessToken(refreshAccessTokenRequestDto));
    }

    @Operation(
            summary = "Logs out the user",
            description = "Invalidates the provided refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged out"),
            @ApiResponse(responseCode = "404", description = "Refresh token was not found")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestBody @Valid LogoutRequestDto logoutRequestDto) {
        authService.logout(logoutRequestDto);
        return ResponseEntity.ok("User successfully logged out.");
    }

}
