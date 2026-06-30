package com.farmeasy.authentication.controller;

import com.farmeasy.authentication.dto.AuditLogDTO;
import com.farmeasy.authentication.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    @Operation(summary = "Log a user event for auditing", description = "Logs user events such as LOGIN_SUCCESS, PASSWORD_RESET_REQUEST, LOGOUT, etc.")
    @PostMapping("/log")
    public ResponseEntity<Void> logEvent(@RequestBody AuditLogDTO auditLogDTO) {
        auditService.logEvent(auditLogDTO.getUserId(), auditLogDTO.getEventType(), auditLogDTO.getIpAddress(), auditLogDTO.getUserAgent());
        return ResponseEntity.noContent().build();
    }
}