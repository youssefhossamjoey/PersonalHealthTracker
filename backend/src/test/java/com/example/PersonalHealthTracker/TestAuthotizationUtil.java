package com.example.personalhealthtracker;

import com.example.personalhealthtracker.domain.dto.AuthResponse;
import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.Role;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public final class TestAuthotizationUtil {
    private TestAuthotizationUtil(){}

    /**
     * Registers a temporary user and returns HttpHeaders containing
     * the Authorization Bearer header and Cookie header with refresh token.
     */
    public static HttpHeaders createAuthorization(TestRestTemplate restTemplate) {
        String username = "testuser_" + UUID.randomUUID().toString().substring(0,8);
        String password = "Password123!";

        UserAccount user = new UserAccount(username, password, Role.MEMBER);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/api/v1/auth/register", user, AuthResponse.class);

        HttpHeaders headers = new HttpHeaders();
        if (response.getBody() != null && response.getBody().getToken() != null) {
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getBody().getToken());
        }

        String setCookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (setCookie != null) {
            // extract the cookie name=value portion (before ';')
            String cookiePair = setCookie.split(";", 2)[0];
            headers.add(HttpHeaders.COOKIE, cookiePair);
        }

        return headers;
    }
}
