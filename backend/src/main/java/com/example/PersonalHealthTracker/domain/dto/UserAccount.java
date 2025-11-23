package com.example.personalhealthtracker.domain.dto;

import com.example.personalhealthtracker.domain.entities.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount {

    private UUID id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Role role;
    private LocalDateTime createdAt;

    public UserAccount(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
