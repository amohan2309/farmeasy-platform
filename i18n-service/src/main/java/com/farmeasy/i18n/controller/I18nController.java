package com.farmeasy.i18n.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.farmeasy.i18n.service.LocaleDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/i18n")
@RequiredArgsConstructor
public class I18nController {
    private final LocaleDetectionService localeDetectionService;
    private final ObjectMapper objectMapper;

    @GetMapping("/locale/detect")
    public Map<String, String> detect(@RequestParam double lat, @RequestParam double lng) {
        return localeDetectionService.detect(lat, lng);
    }

    @GetMapping("/messages")
    public Map<String, String> messages(@RequestParam(defaultValue = "en") String locale) throws Exception {
        String file = "i18n/messages_" + (locale.equals("hi") || locale.equals("mr") ? "hi" : "en") + ".json";
        try (InputStream in = new ClassPathResource(file).getInputStream()) {
            return objectMapper.readValue(in, new TypeReference<>() {});
        }
    }
}
