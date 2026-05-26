package com.farmeasy.subscription.controller;

import com.farmeasy.subscription.service.UsageMeteringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/usage")
@RequiredArgsConstructor
public class UsageController {
    private final UsageMeteringService meteringService;

    @GetMapping("/image-analysis")
    public Map<String, Object> imageUsage(@RequestHeader("X-User-Id") String userId) {
        return meteringService.getImageUsage(UUID.fromString(userId));
    }

    @PostMapping("/image-analysis/consume")
    public Map<String, Object> consume(@RequestHeader("X-User-Id") String userId) {
        boolean ok = meteringService.tryConsumeImageAnalysis(UUID.fromString(userId));
        if (!ok) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Daily image analysis limit reached. Upgrade to Pro.");
        }
        return meteringService.getImageUsage(UUID.fromString(userId));
    }
}
