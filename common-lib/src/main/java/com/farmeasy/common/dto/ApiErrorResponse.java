package com.farmeasy.common.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiErrorResponse {
    private String code;
    private String message;
    private Instant timestamp;
}
