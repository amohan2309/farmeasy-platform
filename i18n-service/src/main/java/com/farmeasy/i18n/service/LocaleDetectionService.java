package com.farmeasy.i18n.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LocaleDetectionService {

    private static final Map<String, String> STATE_TO_LOCALE = Map.ofEntries(
            Map.entry("Maharashtra", "mr"),
            Map.entry("Gujarat", "gu"),
            Map.entry("Karnataka", "kn"),
            Map.entry("Tamil Nadu", "ta"),
            Map.entry("Kerala", "ml"),
            Map.entry("Punjab", "pa"),
            Map.entry("West Bengal", "bn"),
            Map.entry("Odisha", "or"),
            Map.entry("Telangana", "te"),
            Map.entry("Andhra Pradesh", "te"),
            Map.entry("Rajasthan", "hi"),
            Map.entry("Uttar Pradesh", "hi"),
            Map.entry("Bihar", "hi"),
            Map.entry("Madhya Pradesh", "hi"),
            Map.entry("Delhi", "hi")
    );

    public Map<String, String> detect(double latitude, double longitude) {
        String state = approximateState(latitude, longitude);
        String locale = STATE_TO_LOCALE.getOrDefault(state, "hi");
        return Map.of(
                "state", state,
                "locale", locale,
                "fallbackLocale", "en"
        );
    }

    private String approximateState(double lat, double lng) {
        if (lat >= 15.5 && lat <= 22.0 && lng >= 72.5 && lng <= 80.5) return "Maharashtra";
        if (lat >= 22.0 && lat <= 30.5 && lng >= 68.0 && lng <= 74.5) return "Gujarat";
        if (lat >= 11.5 && lat <= 18.5 && lng >= 74.0 && lng <= 78.5) return "Karnataka";
        if (lat >= 8.0 && lat <= 13.5 && lng >= 76.0 && lng <= 80.5) return "Tamil Nadu";
        if (lat >= 26.0 && lat <= 31.5 && lng >= 74.0 && lng <= 77.5) return "Punjab";
        if (lat >= 24.0 && lat <= 28.5 && lng >= 86.0 && lng <= 89.5) return "West Bengal";
        return "Uttar Pradesh";
    }
}
