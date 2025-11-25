package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByToken(String token);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete from RefreshTokenEntity r where r.user.id = :userId")
    void deleteByUserId(UUID userId);

    @Transactional
    void deleteByToken(String token);

    List<RefreshTokenEntity> findAllByUser_Id(UUID userId);
}
