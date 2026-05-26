package com.farmeasy.authentication.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class DeviceRegisterDTO {
    private UUID userId;
    private String deviceName;
    private String deviceType;
    private String ipAddress;
    private String userAgent;
}
