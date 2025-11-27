package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.AuthResponse;
import com.example.personalhealthtracker.domain.dto.LoginRequest;
import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;

import com.example.personalhealthtracker.domain.entities.Role;
import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import com.example.personalhealthtracker.mappers.impl.UserAccountMapper;
import com.example.personalhealthtracker.security.UserAccountDetails;
import com.example.personalhealthtracker.services.AuthenticationService;
import com.example.personalhealthtracker.services.RefreshTokenService;
import com.example.personalhealthtracker.services.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;
    private final UserAccountService userAccountService;
    private final UserAccountMapper userAccountMapper;

    @Value("${jwt.expiry-ms}")
    private Long jwtExpiryMs;

    //samesite and secure defined in application properties for configurability
    @Value("${app.refresh-token-cookie-samesite}")
    private String sameSite;
    @Value("${app.refresh-token-cookie-secure}")
    private boolean secure;

    @GetMapping(path ="/username-available")
    public ResponseEntity<Boolean> CheckAvailable(@RequestParam String username ){
        boolean taken = userAccountService.isAvaialble(username);
        return ResponseEntity.ok(!taken);
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> createUserAccount(@Valid @RequestBody UserAccount userAccount, UriComponentsBuilder ucb) {
        userAccount.setRole(Role.MEMBER);
        UserAccountEntity createdUserAccount = userAccountService.creatUserAccount(userAccountMapper.mapFrom(userAccount));
        UserAccountDetails details = new UserAccountDetails(createdUserAccount);

        // create refresh token for this user
        var userId = createdUserAccount.getId();

        //createRefreshTokenForUser() deletes existing tokens before creation
        var refreshTokenEntity = refreshTokenService.createRefreshTokenForUser(userId);


        // set refresh token as HttpOnly cookie
        long maxAge = Math.max(0, refreshTokenEntity.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond());
        ResponseCookie cookie = buildRefreshCookie(refreshTokenEntity.getToken(), maxAge);

        URI locationOfNewUserAccount = ucb
                .path("home/{id}")
                .buildAndExpand(createdUserAccount.getId())
                .toUri();

        return ResponseEntity
                .created(locationOfNewUserAccount)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(buildAuthResponse(details));
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        UserAccountDetails userAccountDetails = authenticationService.authenticate(
                loginRequest.getUsername(),
                loginRequest.getPassword());

        // create refresh token for this user
        var userId = userAccountDetails.getUser().getId();

        //createRefreshTokenForUser() deletes existing tokens before creation
        var refreshTokenEntity = refreshTokenService.createRefreshTokenForUser(userId);


        // set refresh token as HttpOnly cookie
        long maxAge = Math.max(0, refreshTokenEntity.getExpiryDate().getEpochSecond() - Instant.now().getEpochSecond());

        ResponseCookie cookie = buildRefreshCookie(refreshTokenEntity.getToken(), maxAge);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(buildAuthResponse(userAccountDetails));

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(value = "refreshToken", required = true) String refreshTokenCookie) {

        refreshTokenService.deleteByToken(refreshTokenCookie);
        // Clear refresh token cookie
        ResponseCookie cookie = buildRefreshCookie("", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }


    @PostMapping(path = "/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @CookieValue(value = "refreshToken", required = true) String refreshTokenCookie) {

        RefreshTokenEntity oldToken = refreshTokenService.findValidTokenOrThrow(refreshTokenCookie);

        UserAccountDetails details = new UserAccountDetails(oldToken.getUser());

        //createRefreshTokenForUser() deletes existing tokens before creation
        RefreshTokenEntity newRefreshToken = refreshTokenService.createRefreshTokenForUser(details.getId());

        long maxAgeSeconds = Duration.between(Instant.now(), newRefreshToken.getExpiryDate())
                .getSeconds();

        ResponseCookie cookie2 = buildRefreshCookie(newRefreshToken.getToken(), maxAgeSeconds);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie2.toString())
                .body(buildAuthResponse(details));

    }

    private ResponseCookie buildRefreshCookie(String token, long maxAgeSeconds) {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite(sameSite)
                .build();
    }

    private AuthResponse buildAuthResponse(UserAccountDetails details) {
        String newAccessToken = authenticationService.generateToken(details);
        return AuthResponse.builder()
                .token(newAccessToken)
                .expiresin(jwtExpiryMs)
                .build();
    }

}
