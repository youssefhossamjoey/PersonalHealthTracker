package com.example.personalhealthtracker.services;

import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAccountService {

    UserAccountEntity creatUserAccount(UserAccountEntity userAccountEntity);

    List<UserAccountEntity> findAll();

    Page<UserAccountEntity> findAll(Pageable pageable);

    Optional<UserAccountEntity> findOne(UUID id);


}
