package com.farmeasy.catalog.repository;

import com.farmeasy.catalog.entity.FarmApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FarmAppRepository extends JpaRepository<FarmApp, UUID> {
    List<FarmApp> findByActiveTrueOrderBySortOrderAsc();
}
