package com.client.onboarding.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;  // Email for which the token was generated

    @Column(unique = true)
    private String token;  // Encrypted token

    private LocalDateTime expiryDate; // Expiration time (5 minutes from creation)

}
