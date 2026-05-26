package com.farmeasy.authentication.dto;

import lombok.Data;
import java.util.Set;
import java.util.UUID;

@Data
public class RoleAssignRequestDTO {
    private UUID userId;
    private Set<String> roles;
}
