package com.example.personalhealthtracker.services.impl;

import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;
import com.example.personalhealthtracker.repositories.RefreshTokenRepository;
import com.example.personalhealthtracker.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpirationMs; // e.g. 7 days = 604800000

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshTokenEntity createRefreshToken(String username) {
        return null;
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        return null;
    }

    @Override
    public RefreshTokenEntity getToken(String token) {
        return null;
    }
}
