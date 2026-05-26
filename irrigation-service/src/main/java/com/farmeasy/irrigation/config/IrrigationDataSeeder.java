package com.farmeasy.irrigation.config;

import com.farmeasy.irrigation.entity.IrrigationSchedule;
import com.farmeasy.irrigation.repository.IrrigationScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IrrigationDataSeeder implements CommandLineRunner {
    private final IrrigationScheduleRepository scheduleRepo;

    @Override
    public void run(String... args) {
        if (scheduleRepo.count() > 0) return;
        UUID demoUser = UUID.fromString("00000000-0000-0000-0000-000000000001");
        scheduleRepo.save(IrrigationSchedule.builder()
                .userId(demoUser)
                .cropType("Wheat")
                .startTime(LocalTime.of(6, 0))
                .durationMinutes(45)
                .active(true)
                .autoMode(true)
                .build());
    }
}
