package com.farmeasy.common.dto;

import com.farmeasy.common.plan.PlanCode;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class EntitlementsResponse {
    private PlanCode planCode;
    private Set<String> features;
    private Map<String, Long> limits;
}
