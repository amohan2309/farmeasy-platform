package com.farmeasy.cropselling.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "crop_listings", schema = "crop_selling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropListing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID sellerId;

    @Column(nullable = false)
    private String cropName;

    private String cropNameHi;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantityKg;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerKg;

    private String location;
    private String buyerType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ListingStatus status = ListingStatus.ACTIVE;
}
