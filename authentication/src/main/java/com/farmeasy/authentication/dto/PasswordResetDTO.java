package com.farmeasy.authentication.dto;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String resetToken;
    private String newPassword;
}
