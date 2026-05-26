package com.farmeasy.marketplace.controller;

import com.farmeasy.marketplace.entity.*;
import com.farmeasy.marketplace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/marketplace")
@RequiredArgsConstructor
public class MarketplaceController {
    private final ProductRepository productRepo;
    private final EquipmentBookingRepository bookingRepo;

    @GetMapping("/products")
    public List<Product> listProducts(@RequestParam(required = false) ProductCategory category) {
        if (category != null) {
            return productRepo.findByCategoryAndInStockTrueOrderByNameEnAsc(category);
        }
        return productRepo.findAll().stream().filter(Product::isInStock).toList();
    }

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productRepo.findById(id).orElseThrow();
    }

    @PostMapping("/equipment/bookings")
    public EquipmentBooking bookEquipment(@RequestBody EquipmentBookingRequest req) {
        Product product = productRepo.findById(req.productId()).orElseThrow();
        return bookingRepo.save(EquipmentBooking.builder()
                .userId(req.userId())
                .productId(req.productId())
                .bookingDate(req.bookingDate())
                .equipmentName(product.getNameEn())
                .status(BookingStatus.CONFIRMED)
                .build());
    }

    @GetMapping("/equipment/bookings")
    public List<EquipmentBooking> userBookings(@RequestHeader("X-User-Id") UUID userId) {
        return bookingRepo.findByUserIdOrderByBookingDateDesc(userId);
    }

    public record EquipmentBookingRequest(UUID userId, UUID productId, java.time.LocalDate bookingDate) {}
}
