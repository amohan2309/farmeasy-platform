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
@Table(name = "auth_audit", schema = "authentication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthAudit {
    @Id
    @Column(name = "audit_id", columnDefinition = "UUID")
    private UUID auditId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String eventType;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime eventTime;
}
