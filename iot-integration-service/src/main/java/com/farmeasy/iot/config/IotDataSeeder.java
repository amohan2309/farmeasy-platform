package com.farmeasy.iot.config;

import com.farmeasy.iot.entity.*;
import com.farmeasy.iot.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IotDataSeeder implements CommandLineRunner {
    private final FarmDeviceRepository deviceRepo;
    private final SensorReadingRepository readingRepo;

    @Override
    public void run(String... args) {
        if (deviceRepo.count() > 0) return;

        String chipId = "FE-CHIP-001";
        UUID demoUser = UUID.fromString("00000000-0000-0000-0000-000000000001");

        deviceRepo.save(FarmDevice.builder()
                .chipId(chipId)
                .userId(demoUser)
                .farmName("Demo Wheat Field")
                .status(DeviceStatus.ONLINE)
                .lastSeenAt(Instant.now())
                .solarPowered(true)
                .build());

        readingRepo.save(SensorReading.builder()
                .chipId(chipId)
                .soilMoisturePercent(42.5)
                .soilPh(6.8)
                .npkN(45)
                .npkP(22)
                .npkK(38)
                .temperatureCelsius(28.3)
                .humidityPercent(61)
                .recordedAt(Instant.now())
                .build());
    }
}
