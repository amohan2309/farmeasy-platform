package com.farmeasy.authentication.dto;

import lombok.Data;

@Data
public class PhoneOtpVerifyDTO {
    private String phoneNumber;
    private String otpCode;
}
