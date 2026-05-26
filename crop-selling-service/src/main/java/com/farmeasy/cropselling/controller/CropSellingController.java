package com.farmeasy.cropselling.controller;

import com.farmeasy.cropselling.entity.*;
import com.farmeasy.cropselling.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/crop-selling")
@RequiredArgsConstructor
public class CropSellingController {
    private final CropListingRepository listingRepo;
    private final CropOrderRepository orderRepo;

    @GetMapping("/listings")
    public List<CropListing> activeListings() {
        return listingRepo.findByStatusOrderByCropNameAsc(ListingStatus.ACTIVE);
    }

    @GetMapping("/listings/mine")
    public List<CropListing> myListings(@RequestHeader("X-User-Id") UUID sellerId) {
        return listingRepo.findBySellerIdOrderByCropNameAsc(sellerId);
    }

    @PostMapping("/listings")
    public CropListing createListing(@RequestBody CropListing listing) {
        listing.setStatus(ListingStatus.ACTIVE);
        return listingRepo.save(listing);
    }

    @PostMapping("/orders")
    public CropOrder placeOrder(@RequestBody PlaceOrderRequest req) {
        CropListing listing = listingRepo.findById(req.listingId()).orElseThrow();
        BigDecimal total = listing.getPricePerKg().multiply(req.quantityKg());
        return orderRepo.save(CropOrder.builder()
                .listingId(req.listingId())
                .buyerId(req.buyerId())
                .quantityKg(req.quantityKg())
                .totalAmountInr(total)
                .status(OrderStatus.PLACED)
                .build());
    }

    @GetMapping("/orders")
    public List<CropOrder> myOrders(@RequestHeader("X-User-Id") UUID buyerId) {
        return orderRepo.findByBuyerIdOrderByCreatedAtDesc(buyerId);
    }

    public record PlaceOrderRequest(UUID listingId, UUID buyerId, BigDecimal quantityKg) {}
}
