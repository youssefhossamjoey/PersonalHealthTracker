package com.example.personalhealthtracker.services.impl;

import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;
import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import com.example.personalhealthtracker.exceptions.ExpiredRefreshTokenException;
import com.example.personalhealthtracker.exceptions.InvalidRefreshTokenException;
import com.example.personalhealthtracker.exceptions.RevokedRefreshTokenException;
import com.example.personalhealthtracker.repositories.RefreshTokenRepository;
import com.example.personalhealthtracker.repositories.UserAccountRepository;
import com.example.personalhealthtracker.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserAccountRepository userAccountRepository;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenDurationMs;

    // Use a single SecureRandom instance for token generation
    private static final class SecureRandomHolder {
        static final SecureRandom SECURE_RANDOM = new SecureRandom();
    }

    @Override
    public RefreshTokenEntity createRefreshTokenForUser(UUID userId) {
        UserAccountEntity user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        deleteTokens(user);
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setRevoked(false);
        // generate cryptographically secure random token (URL-safe, no padding)
        byte[] randomBytes = new byte[64];
        SecureRandomHolder.SECURE_RANDOM.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        refreshToken.setToken(token);
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    @Override
    public RefreshTokenEntity findValidTokenOrThrow(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(InvalidRefreshTokenException::new);
        if (!refreshToken.getRevoked()) {
            return verifyExpiration(refreshToken);
        } else {
            throw new RevokedRefreshTokenException();
        }

    }

    private RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new ExpiredRefreshTokenException();
        }
        return token;
    }

    public void deleteTokens(UserAccountEntity user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
