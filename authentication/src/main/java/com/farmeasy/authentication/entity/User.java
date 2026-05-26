package com.farmeasy.authentication.entity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users", schema = "authentication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "user_id", columnDefinition = "UUID")
    private UUID userId;

    private String username;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private String salt;
    private boolean isActive;
    private boolean isVerified;
    private boolean mfaEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        schema = "authentication",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<UserDevice> devices;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AuthToken> tokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<OtpVerification> otps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PasswordReset> passwordResets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AuthAudit> audits;
}
