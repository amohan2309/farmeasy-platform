package com.farmeasy.liveprices.controller;

import com.farmeasy.liveprices.entity.MandiPrice;
import com.farmeasy.liveprices.repository.MandiPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class LivePricesController {
    private final MandiPriceRepository priceRepo;

    @GetMapping("/mandi")
    public List<MandiPrice> allPrices() {
        return priceRepo.findAllByOrderByPriceDateDesc();
    }

    @GetMapping("/mandi/search")
    public List<MandiPrice> search(@RequestParam String crop) {
        return priceRepo.findByCropNameContainingIgnoreCaseOrderByPriceDateDesc(crop);
    }
}
