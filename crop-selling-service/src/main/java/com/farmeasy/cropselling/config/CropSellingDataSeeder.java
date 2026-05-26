package com.farmeasy.cropselling.config;

import com.farmeasy.cropselling.entity.CropListing;
import com.farmeasy.cropselling.entity.ListingStatus;
import com.farmeasy.cropselling.repository.CropListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CropSellingDataSeeder implements CommandLineRunner {
    private final CropListingRepository listingRepo;

    @Override
    public void run(String... args) {
        if (listingRepo.count() > 0) return;
        UUID seller = UUID.fromString("00000000-0000-0000-0000-000000000002");
        listingRepo.save(CropListing.builder()
                .sellerId(seller)
                .cropName("Wheat")
                .cropNameHi("गेहूं")
                .quantityKg(new BigDecimal("5000"))
                .pricePerKg(new BigDecimal("24.50"))
                .location("Ludhiana, Punjab")
                .buyerType("Factory")
                .status(ListingStatus.ACTIVE)
                .build());
        listingRepo.save(CropListing.builder()
                .sellerId(seller)
                .cropName("Basmati Rice")
                .cropNameHi("बासमती चावल")
                .quantityKg(new BigDecimal("2000"))
                .pricePerKg(new BigDecimal("68"))
                .location("Karnal, Haryana")
                .buyerType("Mandi")
                .status(ListingStatus.ACTIVE)
                .build());
    }
}
