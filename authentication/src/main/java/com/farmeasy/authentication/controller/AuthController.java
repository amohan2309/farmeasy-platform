package com.farmeasy.authentication.controller;

import com.farmeasy.authentication.dto.*;
import com.farmeasy.authentication.service.AuthService;
import com.farmeasy.authentication.service.PhoneOtpAuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final PhoneOtpAuthService phoneOtpAuthService;

    @Operation(summary = "Login", description = "Login with username/email and password. Returns JWT access and refresh tokens.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @Operation(summary = "Logout", description = "Logout and invalidate the refresh token.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LoginResponseDTO dto) {
        authService.logout(dto.getRefreshToken());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Refresh access token", description = "Refresh access token using a valid refresh token.")
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(@RequestBody LoginResponseDTO dto) {
        return ResponseEntity.ok(authService.refreshToken(dto.getRefreshToken()));
    }

    @Operation(summary = "Send OTP to phone", description = "Sends OTP for phone-based login (Pro plan feature in production).")
    @PostMapping("/phone/send-otp")
    public ResponseEntity<Void> sendPhoneOtp(@RequestBody PhoneOtpRequestDTO dto) {
        phoneOtpAuthService.sendOtp(dto.getPhoneNumber());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verify phone OTP and login")
    @PostMapping("/phone/verify")
    public ResponseEntity<LoginResponseDTO> verifyPhoneOtp(@RequestBody PhoneOtpVerifyDTO dto) {
        return ResponseEntity.ok(phoneOtpAuthService.verifyAndLogin(dto.getPhoneNumber(), dto.getOtpCode()));
    }
}