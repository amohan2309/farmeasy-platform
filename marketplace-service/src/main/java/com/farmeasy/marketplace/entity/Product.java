package com.farmeasy.marketplace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products", schema = "marketplace")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Column(nullable = false)
    private String nameEn;

    private String nameHi;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal priceInr;

    @Column(nullable = false)
    private String unit;

    private String supplier;

    @Builder.Default
    private boolean inStock = true;

    private String imageUrl;
}
