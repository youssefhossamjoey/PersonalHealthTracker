package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    void deleteByUserId(String userId);

}
