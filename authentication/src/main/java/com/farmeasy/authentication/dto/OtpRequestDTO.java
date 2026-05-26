package com.farmeasy.authentication.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OtpRequestDTO {
    private UUID userId;
    private String deliveryMethod;
}
