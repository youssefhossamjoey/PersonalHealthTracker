package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.AuthResponse;
import com.example.personalhealthtracker.domain.dto.LoginRequest;
import com.example.personalhealthtracker.security.UserAccountDetails;
import com.example.personalhealthtracker.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;


    @RequestMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        UserAccountDetails userAccountDetails = authenticationService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        String tokenValue = authenticationService.generateToken(userAccountDetails);
        AuthResponse authResponse = AuthResponse.builder()
                .token(tokenValue)
                .expiresin(86400)
                .build();
        return ResponseEntity.ok(authResponse);

    }
}
