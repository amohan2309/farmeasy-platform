package com.farmeasy.authentication.dto;

import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    private String emailOrUsername;
}
