package com.farmeasy.liveprices.config;

import com.farmeasy.liveprices.entity.MandiPrice;
import com.farmeasy.liveprices.entity.PriceTrend;
import com.farmeasy.liveprices.repository.MandiPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LivePricesDataSeeder implements CommandLineRunner {
    private final MandiPriceRepository priceRepo;

    @Override
    public void run(String... args) {
        if (priceRepo.count() > 0) return;
        LocalDate today = LocalDate.now();
        priceRepo.save(MandiPrice.builder().mandiName("Khanna Mandi").cropName("Wheat")
                .cropNameHi("गेहूं").pricePerQuintal(new BigDecimal("2450")).priceDate(today).trend(PriceTrend.UP).build());
        priceRepo.save(MandiPrice.builder().mandiName("Karnal Mandi").cropName("Basmati Rice")
                .cropNameHi("बासमती").pricePerQuintal(new BigDecimal("6800")).priceDate(today).trend(PriceTrend.STABLE).build());
        priceRepo.save(MandiPrice.builder().mandiName("Muzaffarnagar").cropName("Sugarcane")
                .cropNameHi("गन्ना").pricePerQuintal(new BigDecimal("350")).priceDate(today).trend(PriceTrend.DOWN).build());
    }
}
