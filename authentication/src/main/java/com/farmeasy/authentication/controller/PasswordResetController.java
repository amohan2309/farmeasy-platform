package com.farmeasy.authentication.controller;

import com.farmeasy.authentication.dto.PasswordResetDTO;
import com.farmeasy.authentication.dto.PasswordResetRequestDTO;
import com.farmeasy.authentication.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @Operation(summary = "Request password reset", description = "Requests a password reset and generates a reset token.")
    @PostMapping("/request")
    public ResponseEntity<Void> requestReset(@RequestBody PasswordResetRequestDTO dto) {
        passwordResetService.requestReset(dto.getEmailOrUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reset password using token", description = "Resets the password using a valid reset token.")
    @PostMapping("/reset")
    public ResponseEntity<Boolean> resetPassword(@RequestBody PasswordResetDTO dto) {
        boolean result = passwordResetService.resetPassword(dto.getResetToken(), dto.getNewPassword());
        return ResponseEntity.ok(result);
    }
}