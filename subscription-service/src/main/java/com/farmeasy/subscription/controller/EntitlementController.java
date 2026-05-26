package com.farmeasy.subscription.controller;

import com.farmeasy.common.dto.EntitlementsResponse;
import com.farmeasy.subscription.service.EntitlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/entitlements")
@RequiredArgsConstructor
public class EntitlementController {
    private final EntitlementService entitlementService;

    @GetMapping("/me")
    public EntitlementsResponse me(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        return entitlementService.getForUser(parseUserId(userId));
    }

    private UUID parseUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "X-User-Id header required");
        }
        return UUID.fromString(userId);
    }
}
