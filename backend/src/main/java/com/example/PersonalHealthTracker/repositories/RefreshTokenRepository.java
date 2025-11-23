package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;
import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByToken(String token);

    @Transactional
    void  deleteByUser(UserAccountEntity user);
    @Transactional
    void  deleteByToken(String token);
}
