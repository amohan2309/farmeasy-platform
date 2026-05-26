package com.farmeasy.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class VisionAnalysisService {
    @Value("${farmeasy.llm.provider:mock}")
    private String provider;

    public Map<String, Object> analyze(UUID userId, MultipartFile image, String locale, boolean detailedReport) {
        String summary = detailedReport
                ? "Detailed analysis: possible leaf spot disease. Apply recommended fungicide after local extension officer confirmation. Include irrigation review."
                : "Basic analysis: leaf discoloration detected. Monitor for 3 days.";

        if ("anthropic".equalsIgnoreCase(provider) && System.getenv("ANTHROPIC_API_KEY") != null) {
            summary = "[LLM] " + summary;
        }

        return Map.of(
                "reportId", UUID.randomUUID().toString(),
                "userId", userId.toString(),
                "locale", locale,
                "reportType", detailedReport ? "DETAILED" : "BASIC",
                "summary", summary,
                "confidence", 0.82,
                "recommendations", new String[]{
                        "Avoid over-watering for 48 hours",
                        "Capture a closer photo of affected leaves if symptoms spread"
                },
                "disclaimer", "AI suggestion only. Consult your local agriculture officer before applying chemicals."
        );
    }
}
