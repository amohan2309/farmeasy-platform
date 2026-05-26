package com.farmeasy.authentication.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OtpVerifyDTO {
    private UUID userId;
    private String otpCode;
}
