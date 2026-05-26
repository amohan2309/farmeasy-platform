package com.farmeasy.iot.repository;

import com.farmeasy.iot.entity.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SensorReadingRepository extends JpaRepository<SensorReading, UUID> {
    List<SensorReading> findTop20ByChipIdOrderByRecordedAtDesc(String chipId);
}
