package com.farmeasy.subscription.controller;

import com.farmeasy.common.plan.PlanCode;
import com.farmeasy.subscription.entity.UserSubscription;
import com.farmeasy.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {
    private final UserSubscriptionRepository repository;

    @GetMapping("/plans")
    public List<Map<String, Object>> plans() {
        return List.of(
                Map.of("code", "FREE", "name", "Free Plan", "priceInr", 0),
                Map.of("code", "PRO", "name", "Pro Plan", "priceInr", 799),
                Map.of("code", "BASIC", "name", "Basic", "priceInr", 799),
                Map.of("code", "STANDARD", "name", "Standard", "priceInr", 1999),
                Map.of("code", "PREMIUM", "name", "Premium", "priceInr", 4999)
        );
    }

    @PutMapping("/users/{userId}/plan")
    public UserSubscription assignPlan(@PathVariable UUID userId, @RequestBody Map<String, String> body) {
        PlanCode plan = PlanCode.valueOf(body.get("planCode").toUpperCase());
        UserSubscription sub = repository.findByUserId(userId).orElse(UserSubscription.builder().userId(userId).build());
        sub.setPlanCode(plan);
        return repository.save(sub);
    }

    @GetMapping("/users/{userId}")
    public UserSubscription getUserPlan(@PathVariable UUID userId) {
        return repository.findByUserId(userId)
                .orElse(UserSubscription.builder().userId(userId).planCode(PlanCode.FREE).build());
    }
}
