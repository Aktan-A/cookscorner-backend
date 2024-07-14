package com.neobis.cookscorner.security;

import com.neobis.cookscorner.exception.InvalidRequestException;
import com.neobis.cookscorner.exception.ResourceNotFoundException;
import com.neobis.cookscorner.model.RefreshToken;
import com.neobis.cookscorner.model.User;
import com.neobis.cookscorner.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${security.refresh-token-expire-minutes}")
    private Long refreshTokenExpireMinutes;

    public RefreshToken createRefreshToken(User user) {
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(
                token,
                LocalDateTime.now().plusMinutes(refreshTokenExpireMinutes),
                user
        );
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validateRefreshTokenByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isEmpty()) {
            throw new ResourceNotFoundException("Refresh token not found.");
        }

        RefreshToken refreshTokenModel = refreshToken.get();

        if (refreshTokenModel.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshTokenModel);
            throw new InvalidRequestException("Refresh token has expired.");
        }

        return refreshTokenModel;
    }

    public void deleteRefreshTokenByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
        if (refreshToken.isEmpty()) {
            throw new ResourceNotFoundException("Refresh token not found.");
        }

        RefreshToken refreshTokenModel = refreshToken.get();

        refreshTokenRepository.delete(refreshTokenModel);
    }

}
