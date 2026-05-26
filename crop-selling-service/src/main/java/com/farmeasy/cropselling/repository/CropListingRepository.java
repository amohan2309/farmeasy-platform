package com.farmeasy.cropselling.repository;

import com.farmeasy.cropselling.entity.CropListing;
import com.farmeasy.cropselling.entity.ListingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CropListingRepository extends JpaRepository<CropListing, UUID> {
    List<CropListing> findByStatusOrderByCropNameAsc(ListingStatus status);
    List<CropListing> findBySellerIdOrderByCropNameAsc(UUID sellerId);
}
