package com.farmeasy.authentication.dto;

import java.util.UUID;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private UUID userId;
    private String eventType;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime eventTime;
    
}
