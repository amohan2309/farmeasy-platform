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
@Table(name = "otp_verification", schema = "authentication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerification {
    @Id
    @Column(name = "otp_id", columnDefinition = "UUID")
    private UUID otpId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String otpCode;
    private String deliveryMethod;
    private boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
