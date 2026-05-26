package com.farmeasy.cropselling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "crop_orders", schema = "crop_selling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID listingId;

    private UUID buyerId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantityKg;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmountInr;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PLACED;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
