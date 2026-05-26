package com.farmeasy.authentication.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private java.util.UUID userId;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
}
