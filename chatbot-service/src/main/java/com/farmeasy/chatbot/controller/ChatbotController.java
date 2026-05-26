package com.farmeasy.chatbot.controller;

import com.farmeasy.chatbot.service.SubscriptionClient;
import com.farmeasy.chatbot.service.VisionAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {
    private final SubscriptionClient subscriptionClient;
    private final VisionAnalysisService visionAnalysisService;

    @PostMapping(value = "/analyze", consumes = "multipart/form-data")
    public Map<String, Object> analyze(
            @RequestHeader("X-User-Id") String userId,
            @RequestParam("image") MultipartFile image,
            @RequestParam(defaultValue = "en") String locale,
            @RequestParam(defaultValue = "false") boolean detailedReport) {

        UUID uid = UUID.fromString(userId);
        if (!subscriptionClient.consumeImageQuota(uid)) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED,
                    "Daily image limit reached. Upgrade to Pro for unlimited analysis.");
        }
        if (image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file required");
        }
        return visionAnalysisService.analyze(uid, image, locale, detailedReport);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
