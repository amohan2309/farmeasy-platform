package com.farmeasy.chatbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SubscriptionClient {
    @Value("${farmeasy.subscription-service-url}")
    private String subscriptionUrl;

    private WebClient client() {
        return WebClient.builder().baseUrl(subscriptionUrl).build();
    }

    public boolean consumeImageQuota(UUID userId) {
        try {
            Map<?, ?> res = client().post()
                    .uri("/api/usage/image-analysis/consume")
                    .header("X-User-Id", userId.toString())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return res != null;
        } catch (Exception e) {
            return false;
        }
    }
}
