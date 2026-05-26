package com.farmeasy.subscription.service;

import com.farmeasy.common.plan.PlanCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsageMeteringService {
    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");

    private final StringRedisTemplate redis;
    private final EntitlementService entitlementService;

    public Map<String, Object> getImageUsage(UUID userId) {
        PlanCode plan = entitlementService.getPlanCode(userId);
        long limit = entitlementService.getForUser(userId).getLimits().getOrDefault("max_images_per_day", 5L);
        long used = currentCount(userId);
        boolean allowed = limit < 0 || used < limit;
        return Map.of(
                "used", used,
                "limit", limit,
                "allowed", allowed,
                "planCode", plan.name()
        );
    }

    public boolean tryConsumeImageAnalysis(UUID userId) {
        long limit = entitlementService.getForUser(userId).getLimits().getOrDefault("max_images_per_day", 5L);
        if (limit < 0) {
            increment(userId);
            return true;
        }
        long used = currentCount(userId);
        if (used >= limit) {
            return false;
        }
        increment(userId);
        return true;
    }

    private long currentCount(UUID userId) {
        String val = redis.opsForValue().get(key(userId));
        return val == null ? 0 : Long.parseLong(val);
    }

    private void increment(UUID userId) {
        String k = key(userId);
        Long count = redis.opsForValue().increment(k);
        if (count != null && count == 1) {
            redis.expire(k, secondsUntilMidnightIst());
        }
    }

    private String key(UUID userId) {
        return "usage:image:" + userId + ":" + LocalDate.now(IST);
    }

    private Duration secondsUntilMidnightIst() {
        var now = java.time.ZonedDateTime.now(IST);
        var midnight = now.toLocalDate().plusDays(1).atStartOfDay(IST);
        return Duration.between(now, midnight);
    }
}
