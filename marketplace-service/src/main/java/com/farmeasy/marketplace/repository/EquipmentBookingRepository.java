package com.farmeasy.marketplace.repository;

import com.farmeasy.marketplace.entity.EquipmentBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EquipmentBookingRepository extends JpaRepository<EquipmentBooking, UUID> {
    List<EquipmentBooking> findByUserIdOrderByBookingDateDesc(UUID userId);
}
