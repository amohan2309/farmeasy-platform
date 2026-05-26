package com.farmeasy.subscription.config;

import com.farmeasy.common.plan.EntitlementKeys;
import com.farmeasy.common.plan.PlanCode;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public final class PlanEntitlements {
    private PlanEntitlements() {}

    private static final Map<PlanCode, Set<String>> FEATURES = new EnumMap<>(PlanCode.class);
    private static final Map<PlanCode, Map<String, Long>> LIMITS = new EnumMap<>(PlanCode.class);

    static {
        FEATURES.put(PlanCode.FREE, Set.of(EntitlementKeys.IMAGE_ANALYSIS));
        LIMITS.put(PlanCode.FREE, Map.of(
                "max_images_per_day", 5L,
                "storage_bytes", 104857600L
        ));

        FEATURES.put(PlanCode.PRO, Set.of(
                EntitlementKeys.IMAGE_ANALYSIS,
                EntitlementKeys.LAYER3_UI,
                EntitlementKeys.PHONE_OTP_LOGIN,
                EntitlementKeys.AADHAAR_LOGIN,
                EntitlementKeys.DETAILED_REPORT
        ));
        LIMITS.put(PlanCode.PRO, Map.of(
                "max_images_per_day", -1L,
                "storage_bytes", 10737418240L
        ));

        FEATURES.put(PlanCode.BASIC, Set.of(EntitlementKeys.IMAGE_ANALYSIS));
        LIMITS.put(PlanCode.BASIC, Map.of("max_images_per_day", 50L, "history_days", 30L));

        FEATURES.put(PlanCode.STANDARD, Set.of(
                EntitlementKeys.IMAGE_ANALYSIS,
                EntitlementKeys.LAYER3_UI,
                EntitlementKeys.PHONE_OTP_LOGIN
        ));
        LIMITS.put(PlanCode.STANDARD, Map.of("max_images_per_day", 50L, "storage_bytes", 53687091200L));

        FEATURES.put(PlanCode.PREMIUM, Set.of(
                EntitlementKeys.IMAGE_ANALYSIS,
                EntitlementKeys.LAYER3_UI,
                EntitlementKeys.PHONE_OTP_LOGIN,
                EntitlementKeys.AADHAAR_LOGIN,
                EntitlementKeys.DETAILED_REPORT,
                EntitlementKeys.API_ACCESS
        ));
        LIMITS.put(PlanCode.PREMIUM, Map.of(
                "max_images_per_day", -1L,
                "storage_bytes", 1099511627776L
        ));
    }

    public static Set<String> features(PlanCode plan) {
        return FEATURES.getOrDefault(plan, FEATURES.get(PlanCode.FREE));
    }

    public static Map<String, Long> limits(PlanCode plan) {
        return LIMITS.getOrDefault(plan, LIMITS.get(PlanCode.FREE));
    }
}
