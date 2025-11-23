package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.LoginRequest;
import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.RefreshTokenEntity;
import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import com.example.personalhealthtracker.mappers.impl.UserAccountMapper;
import com.example.personalhealthtracker.security.UserAccountDetails;
import com.example.personalhealthtracker.services.AuthenticationService;
import com.example.personalhealthtracker.services.RefreshTokenService;
import com.example.personalhealthtracker.services.UserAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private UserAccountMapper userAccountMapper;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void registerUserReturnsAuthResponseAndCookie() {
        // Arrange
        UserAccount userDto = new UserAccount("user-test", "pass-test", null);
        UserAccountEntity savedEntity = new UserAccountEntity();
        savedEntity.setId(UUID.randomUUID());
        savedEntity.setUsername("user-test");

        when(userAccountMapper.mapFrom(userDto)).thenReturn(savedEntity);
        when(userAccountService.creatUserAccount(savedEntity)).thenReturn(savedEntity);
        when(refreshTokenService.createRefreshTokenForUser(savedEntity.getId()))
                .thenReturn(new RefreshTokenEntity(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        savedEntity,"refresh-token", Instant.now().plusSeconds(3600),false));

        UserAccountDetails details = new UserAccountDetails(savedEntity);
        when(authenticationService.generateToken(details)).thenReturn("access-token");

        // Act
        ResponseEntity<?> response = authController.createUserAccount(userDto, null);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getHeaders().getFirst("Set-Cookie")).contains("refresh-token");
        assertThat(response.getBody()).isNotNull();

        verify(userAccountService, times(1)).creatUserAccount(savedEntity);
        verify(refreshTokenService, times(1)).createRefreshTokenForUser(savedEntity.getId());
    }

    @Test
    void loginUserReturnsAuthResponseAndCookie() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("user-test", "pass-test");
        UserAccountEntity userEntity = new UserAccountEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setUsername("user-test");

        UserAccountDetails details = new UserAccountDetails(userEntity);

        when(authenticationService.authenticate("user-test", "pass-test")).thenReturn(details);
        when(refreshTokenService.createRefreshTokenForUser(userEntity.getId()))
                .thenReturn(new RefreshTokenEntity(UUID.fromString("00000000-0000-0000-0000-000000000001"),
                        userEntity,"refresh-token", Instant.now().plusSeconds(3600),false));
        when(authenticationService.generateToken(details)).thenReturn("access-token");

        // Act
        ResponseEntity<?> response = authController.login(loginRequest);

        // Assert
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getHeaders().getFirst("Set-Cookie")).contains("refresh-token");
        assertThat(response.getBody()).isNotNull();

        verify(authenticationService, times(1)).authenticate("user-test", "pass-test");
    }


}
