package com.farmeasy.authentication.controller;

import com.farmeasy.authentication.dto.DeviceRegisterDTO;
import com.farmeasy.authentication.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @Operation(summary = "Register user device", description = "Registers a device for the user.")
    @PostMapping("/register")
    public ResponseEntity<Void> registerDevice(@RequestBody DeviceRegisterDTO dto) {
        deviceService.registerDevice(dto.getUserId(), dto.getDeviceName(), dto.getDeviceType(), dto.getIpAddress(), dto.getUserAgent());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Track last login per device", description = "Updates the last login time for a device.")
    @PutMapping("/last-login/{deviceId}")
    public ResponseEntity<Void> updateLastLogin(@PathVariable UUID deviceId) {
        deviceService.updateLastLogin(deviceId);
        return ResponseEntity.noContent().build();
    }
}