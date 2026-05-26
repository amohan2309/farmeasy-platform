package com.farmeasy.iot.controller;

import com.farmeasy.iot.entity.*;
import com.farmeasy.iot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/iot")
@RequiredArgsConstructor
public class IotController {
    private final FarmDeviceRepository deviceRepo;
    private final SensorReadingRepository readingRepo;

    @GetMapping("/devices")
    public List<FarmDevice> devices(@RequestHeader(value = "X-User-Id", required = false) UUID userId) {
        if (userId != null) {
            return deviceRepo.findByUserId(userId);
        }
        return deviceRepo.findAll();
    }

    @GetMapping("/devices/{chipId}/readings")
    public List<SensorReading> readings(@PathVariable String chipId) {
        return readingRepo.findTop20ByChipIdOrderByRecordedAtDesc(chipId);
    }

    @GetMapping("/devices/{chipId}/status")
    public Map<String, Object> status(@PathVariable String chipId) {
        FarmDevice device = deviceRepo.findByChipId(chipId).orElseThrow();
        SensorReading latest = readingRepo.findTop20ByChipIdOrderByRecordedAtDesc(chipId).stream().findFirst().orElse(null);
        return Map.of(
                "chipId", chipId,
                "farmName", device.getFarmName(),
                "status", device.getStatus().name(),
                "solarPowered", device.isSolarPowered(),
                "latestReading", latest != null ? latest : Map.of()
        );
    }

    @PostMapping("/readings")
    public SensorReading ingest(@RequestBody SensorReading reading) {
        reading.setRecordedAt(reading.getRecordedAt() != null ? reading.getRecordedAt() : Instant.now());
        deviceRepo.findByChipId(reading.getChipId()).ifPresent(d -> {
            d.setLastSeenAt(Instant.now());
            d.setStatus(DeviceStatus.ONLINE);
            deviceRepo.save(d);
        });
        return readingRepo.save(reading);
    }

    @PostMapping("/devices/{chipId}/override")
    public Map<String, String> manualOverride(@PathVariable String chipId, @RequestBody Map<String, String> body) {
        return Map.of("chipId", chipId, "action", body.getOrDefault("action", "NONE"), "status", "ACKNOWLEDGED");
    }
}
