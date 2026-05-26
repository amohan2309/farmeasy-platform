package com.farmeasy.authentication.controller;

import com.farmeasy.authentication.dto.OtpRequestDTO;
import com.farmeasy.authentication.dto.OtpVerifyDTO;
import com.farmeasy.authentication.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @Operation(summary = "Send OTP to user", description = "Sends an OTP to the user via the specified delivery method (sms/email).")
    @PostMapping("/send")
    public ResponseEntity<Void> sendOtp(@RequestBody OtpRequestDTO dto) {
        otpService.sendOtp(dto.getUserId(), dto.getDeliveryMethod());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verify OTP", description = "Verifies the OTP for login or sensitive operations.")
    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyOtp(@RequestBody OtpVerifyDTO dto) {
        boolean result = otpService.verifyOtp(dto.getUserId(), dto.getOtpCode());
        return ResponseEntity.ok(result);
    }
}