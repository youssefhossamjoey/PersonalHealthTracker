package com.example.personalhealthtracker.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private long expiresin;

    private Boolean revoked;


//    // hashed token
//    private LocalDateTime expiresAt;
//    private UUID replacedBy;
}
