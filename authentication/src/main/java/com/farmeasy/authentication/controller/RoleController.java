package com.farmeasy.authentication.controller;

import com.farmeasy.authentication.dto.RoleAssignRequestDTO;
import com.farmeasy.authentication.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @Operation(summary = "Assign roles to a user", description = "Assigns one or more roles to a user.")
    @PostMapping("/assign")
    public ResponseEntity<Void> assignRoles(@RequestBody RoleAssignRequestDTO dto) {
        roleService.assignRoles(dto.getUserId(), dto.getRoles());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Fetch roles of a user", description = "Fetches all roles assigned to a user.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Set<String>> getUserRoles(@PathVariable UUID userId) {
        return ResponseEntity.ok(roleService.getUserRoles(userId));
    }

    @Operation(summary = "Check if user has a specific role", description = "Checks if a user has a specific role.")
    @GetMapping("/user/{userId}/has/{roleName}")
    public ResponseEntity<Boolean> userHasRole(@PathVariable UUID userId, @PathVariable String roleName) {
        return ResponseEntity.ok(roleService.userHasRole(userId, roleName));
    }
}