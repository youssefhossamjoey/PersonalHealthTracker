package com.example.personalhealthtracker.services.impl;

import com.example.personalhealthtracker.security.UserAccountDetailsService;
import com.example.personalhealthtracker.security.UserAccountDetails;
import com.example.personalhealthtracker.services.AuthenticationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserAccountDetailsService userAccountDetailsService;

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    @Value("${jwt.expiry-ms}")
    private Long jwtExpiryMs;

    private Key signingKey;

    @PostConstruct
    private void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKeyBase64);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public UserAccountDetails authenticate(String username, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username,password)
        );
        return (UserAccountDetails) auth.getPrincipal();
    }

    @Override
    public String generateToken(UserAccountDetails userAccountDetails) {
        Map<String, Object> claims= new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userAccountDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiryMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UserAccountDetails validateToken(String token) {
        String username = extractUsername(token);
        return userAccountDetailsService.loadUserByUsername(username);
    }

    private String extractUsername(String token){
       Claims claims= Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

}
