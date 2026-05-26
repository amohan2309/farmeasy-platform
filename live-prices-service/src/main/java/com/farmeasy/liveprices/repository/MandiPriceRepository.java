package com.farmeasy.liveprices.repository;

import com.farmeasy.liveprices.entity.MandiPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MandiPriceRepository extends JpaRepository<MandiPrice, UUID> {
    List<MandiPrice> findByCropNameContainingIgnoreCaseOrderByPriceDateDesc(String crop);
    List<MandiPrice> findAllByOrderByPriceDateDesc();
}
