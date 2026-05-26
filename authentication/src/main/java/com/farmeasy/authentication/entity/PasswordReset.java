package com.farmeasy.authentication.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "password_resets", schema = "authentication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordReset {
    @Id
    @Column(name = "reset_id", columnDefinition = "UUID")
    private UUID resetId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String resetToken;
    private LocalDateTime requestedAt;
    private LocalDateTime expiresAt;
    private boolean isUsed;
}
