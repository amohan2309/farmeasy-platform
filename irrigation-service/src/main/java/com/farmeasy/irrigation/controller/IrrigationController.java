package com.farmeasy.irrigation.controller;

import com.farmeasy.irrigation.entity.IrrigationSchedule;
import com.farmeasy.irrigation.repository.IrrigationScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/irrigation")
@RequiredArgsConstructor
public class IrrigationController {
    private final IrrigationScheduleRepository scheduleRepo;

    @GetMapping("/schedules")
    public List<IrrigationSchedule> schedules(@RequestHeader("X-User-Id") UUID userId) {
        return scheduleRepo.findByUserIdOrderByStartTimeAsc(userId);
    }

    @PostMapping("/schedules")
    public IrrigationSchedule create(@RequestBody IrrigationSchedule schedule) {
        return scheduleRepo.save(schedule);
    }

    @PostMapping("/pump/{action}")
    public Map<String, String> controlPump(@PathVariable String action) {
        return Map.of("status", "PUMP_" + action.toUpperCase(), "message", "Command sent to Smart Chip");
    }

    @GetMapping("/water-usage")
    public Map<String, Object> waterUsage(@RequestHeader(value = "X-User-Id", required = false) UUID userId) {
        return Map.of(
                "todayLiters", 1240,
                "weekLiters", 8920,
                "savedPercent", 18,
                "userId", userId != null ? userId.toString() : "demo"
        );
    }
}
