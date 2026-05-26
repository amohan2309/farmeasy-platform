package com.farmeasy.subscription.service;

import com.farmeasy.common.dto.EntitlementsResponse;
import com.farmeasy.common.plan.PlanCode;
import com.farmeasy.subscription.config.PlanEntitlements;
import com.farmeasy.subscription.entity.UserSubscription;
import com.farmeasy.subscription.repository.UserSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntitlementService {
    private final UserSubscriptionRepository repository;

    @Value("${farmeasy.default-plan:FREE}")
    private PlanCode defaultPlan;

    public EntitlementsResponse getForUser(UUID userId) {
        PlanCode plan = repository.findByUserId(userId)
                .map(UserSubscription::getPlanCode)
                .orElse(defaultPlan);
        return EntitlementsResponse.builder()
                .planCode(plan)
                .features(PlanEntitlements.features(plan))
                .limits(PlanEntitlements.limits(plan))
                .build();
    }

    public PlanCode getPlanCode(UUID userId) {
        return repository.findByUserId(userId)
                .map(UserSubscription::getPlanCode)
                .orElse(defaultPlan);
    }
}
