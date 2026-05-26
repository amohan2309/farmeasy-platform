package com.farmeasy.authentication.dto;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class UserResponseDTO {
    private UUID userId;
    private String username;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private boolean isVerified;
    private boolean mfaEnabled;
    private Set<String> roles;
}
