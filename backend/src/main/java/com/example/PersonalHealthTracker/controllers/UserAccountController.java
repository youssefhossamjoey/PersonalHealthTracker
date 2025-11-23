package com.example.personalhealthtracker.controllers;


import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import com.example.personalhealthtracker.mappers.impl.UserAccountMapper;
import com.example.personalhealthtracker.services.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/useraccount")
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final UserAccountMapper userAccountMapper;

    @Autowired
    public UserAccountController(UserAccountService userAccountService, UserAccountMapper userAccountMapper) {
            this.userAccountMapper = userAccountMapper;
            this.userAccountService = userAccountService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserAccount> getUserAccount(@PathVariable("id") UUID id){
        Optional<UserAccountEntity> requestedUserAccount = userAccountService.findOne(id);
        return requestedUserAccount.map(userAccountEntity ->
                        ResponseEntity.ok(userAccountMapper.mapTo(userAccountEntity)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}

