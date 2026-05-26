package com.farmeasy.irrigation.repository;

import com.farmeasy.irrigation.entity.IrrigationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IrrigationScheduleRepository extends JpaRepository<IrrigationSchedule, UUID> {
    List<IrrigationSchedule> findByUserIdOrderByStartTimeAsc(UUID userId);
}
