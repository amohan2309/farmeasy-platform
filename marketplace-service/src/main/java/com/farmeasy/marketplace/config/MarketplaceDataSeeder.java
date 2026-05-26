package com.farmeasy.marketplace.config;

import com.farmeasy.marketplace.entity.Product;
import com.farmeasy.marketplace.entity.ProductCategory;
import com.farmeasy.marketplace.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MarketplaceDataSeeder implements CommandLineRunner {
    private final ProductRepository productRepo;

    @Override
    public void run(String... args) {
        if (productRepo.count() > 0) return;

        productRepo.save(Product.builder().category(ProductCategory.SEED).nameEn("Wheat HD-2967")
                .nameHi("गेहूं HD-2967").priceInr(new BigDecimal("2450")).unit("per 50kg bag")
                .supplier("FarmEasy Verified").inStock(true).build());
        productRepo.save(Product.builder().category(ProductCategory.SEED).nameEn("Basmati Pusa-1121")
                .nameHi("बासमती Pusa-1121").priceInr(new BigDecimal("5200")).unit("per 50kg bag")
                .supplier("FarmEasy Verified").inStock(true).build());
        productRepo.save(Product.builder().category(ProductCategory.FERTILIZER).nameEn("Urea 46% N")
                .nameHi("यूरिया 46%").priceInr(new BigDecimal("266")).unit("per 45kg bag")
                .supplier("IFFCO Partner").inStock(true).build());
        productRepo.save(Product.builder().category(ProductCategory.PESTICIDE).nameEn("Neem-based Bio Spray")
                .nameHi("नीम जैविक स्प्रे").priceInr(new BigDecimal("890")).unit("per litre")
                .supplier("Organic India").inStock(true).build());
        productRepo.save(Product.builder().category(ProductCategory.EQUIPMENT).nameEn("Tractor Hire (Mahindra 575)")
                .nameHi("ट्रैक्टर किराया").priceInr(new BigDecimal("1200")).unit("per hour")
                .supplier("Local Partner").inStock(true).build());
        productRepo.save(Product.builder().category(ProductCategory.EQUIPMENT).nameEn("Combine Harvester")
                .nameHi("कंबाइन हार्वेस्टर").priceInr(new BigDecimal("4500")).unit("per acre")
                .supplier("FarmEasy Fleet").inStock(true).build());
    }
}
