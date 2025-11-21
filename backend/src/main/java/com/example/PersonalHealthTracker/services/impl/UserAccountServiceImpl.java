package com.example.personalhealthtracker.services.impl;

import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import com.example.personalhealthtracker.repositories.UserAccountRepository;
import com.example.personalhealthtracker.services.UserAccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserAccountServiceImpl implements UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserAccountRepository userAccountRepository,PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserAccountEntity creatUserAccount(UserAccountEntity userAccountEntity) {
        userAccountEntity.setPassword(passwordEncoder.encode(userAccountEntity.getPassword()));
        return userAccountRepository.save(userAccountEntity);
    }

    @Override
    public List<UserAccountEntity> findAll() {
        return StreamSupport.stream(userAccountRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserAccountEntity> findAll(Pageable pageable) {
        return userAccountRepository.findAll(pageable);
    }

    @Override
    public Optional<UserAccountEntity> findOne(UUID id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public Optional<UserAccountEntity> findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }
}
