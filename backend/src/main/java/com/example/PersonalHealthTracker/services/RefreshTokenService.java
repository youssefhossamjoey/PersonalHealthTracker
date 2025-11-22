package com.example.personalhealthtracker.services;

import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;

public interface RefreshTokenService {

    public RefreshTokenEntity createRefreshToken(String username);

    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

    public RefreshTokenEntity getToken(String token);

}
