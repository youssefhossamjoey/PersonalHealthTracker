package com.example.personalhealthtracker.integration;

import com.example.personalhealthtracker.domain.dto.AuthResponse;
import com.example.personalhealthtracker.domain.dto.LoginRequest;
import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.Role;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class UserAccountIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void UserAccountCanBeRegisteredThenLoginAndFetched() {
        // register a new user via auth endpoint
        UserAccount user = new UserAccount("user-test", "pass-test", Role.MEMBER);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/v1/auth/register", user, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // extract id from Location header (AuthController sets location to home/{id})
        URI location = registerResponse.getHeaders().getLocation();
        String path = (location != null) ? location.getPath() : null;
        assertThat(path).isNotNull();
        String[] parts = path.split("/");
        String id = parts[parts.length - 1];

        // login to obtain Authorization token
        var loginReq = new LoginRequest("user-test", "pass-test");
        ResponseEntity<AuthResponse> loginResponse = restTemplate.postForEntity("/api/v1/auth/login", loginReq, AuthResponse.class);
        log.info(loginResponse.toString());
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();
        String token = loginResponse.getBody().getToken();
        assertThat(token).isNotNull();

        // build headers with bearer token and refresh cookie
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        String setCookie = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        if (setCookie == null) {
            setCookie = registerResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        }
        if (setCookie != null) {
            String cookiePair = setCookie.split(";", 2)[0];
            headers.add(HttpHeaders.COOKIE, cookiePair);
        }

        // GET the user account by id
        ResponseEntity<String> getResponse = restTemplate.exchange("/useraccount/" + id, HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext dc = JsonPath.parse(getResponse.getBody());
        Object returnedId = dc.read("$.id");
        String username = dc.read("$.username");
        String role = dc.read("$.role");
        Object password = dc.read("$.password");

        // mapper intentionally hides id and password
        assertThat(returnedId).isNull();
        assertThat(password).isNull();
        assertThat(username).isEqualTo("user-test");
        assertThat(role).isEqualTo("MEMBER");
    }


    @Test
    public void UnauthenticatedAccessIsForbidden() {
        // attempt to fetch a user without any auth headers
        ResponseEntity<String> resp = restTemplate.getForEntity("/useraccount/00000000-0000-0000-0000-000000000001", String.class);
        // should be forbidden for anonymous
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void LoginWithBadCredentialsReturnsUnauthorized() {
        var badLogin = new LoginRequest("nonexistent", "badpass");
        ResponseEntity<AuthResponse> resp = restTemplate.postForEntity("/api/v1/auth/login", badLogin, AuthResponse.class);
        // authentication should fail (unauthorized)
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void RefreshEndpointWorksWithCookieAndHandlesErrors() {
        // register user to obtain a refresh cookie
        UserAccount user = new UserAccount("refresh-user", "refresh-pass", Role.MEMBER);
        ResponseEntity<String> registerResponse = restTemplate.postForEntity("/api/v1/auth/register", user, String.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String setCookie = registerResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        assertThat(setCookie).isNotNull();
        String cookiePair = setCookie.split(";", 2)[0];

        // call refresh endpoint with cookie
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.COOKIE, cookiePair);
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<AuthResponse> refreshResponse = restTemplate.exchange("/api/v1/auth/refresh", HttpMethod.POST, request, AuthResponse.class);
        assertThat(refreshResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(refreshResponse.getBody()).isNotNull();
        assertThat(refreshResponse.getBody().getToken()).isNotNull();

        // Negative: missing cookie -> Bad Request
        ResponseEntity<AuthResponse> missingCookieResp = restTemplate.postForEntity("/api/v1/auth/refresh", null, AuthResponse.class);
        assertThat(missingCookieResp.getStatusCode().is4xxClientError()).isTrue();

        // Negative: invalid cookie -> service should reject (server error or client error depending on handler)
        HttpHeaders badHeaders = new HttpHeaders();
        badHeaders.add(HttpHeaders.COOKIE, "refreshToken=invalidtokenvalue");
        ResponseEntity<String> badResp = restTemplate.exchange("/api/v1/auth/refresh", HttpMethod.POST, new HttpEntity<>(null, badHeaders), String.class);
        // the application throws an InvalidRefreshTokenException which by default maps to 5xx if not handled
        assertThat(badResp.getStatusCode().is5xxServerError() || badResp.getStatusCode().is4xxClientError()).isTrue();
    }

}
