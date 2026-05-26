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
@Table(name = "user_devices", schema = "authentication")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDevice {
    @Id
    @Column(name = "device_id", columnDefinition = "UUID")
    private UUID deviceId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String deviceName;
    private String deviceType;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private boolean isActive;
}
