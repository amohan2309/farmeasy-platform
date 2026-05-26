package com.farmeasy.iot.repository;

import com.farmeasy.iot.entity.FarmDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FarmDeviceRepository extends JpaRepository<FarmDevice, UUID> {
    List<FarmDevice> findByUserId(UUID userId);
    Optional<FarmDevice> findByChipId(String chipId);
}
