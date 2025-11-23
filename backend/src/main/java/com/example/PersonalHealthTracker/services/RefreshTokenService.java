package com.example.personalhealthtracker.services;

import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {

    RefreshTokenEntity createRefreshTokenForUser(UUID userId);

    void deleteByToken(String token);
    
    RefreshTokenEntity findValidTokenOrThrow(String token);
}
