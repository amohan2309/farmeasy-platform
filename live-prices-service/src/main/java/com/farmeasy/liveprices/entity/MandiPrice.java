package com.farmeasy.liveprices.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "mandi_prices", schema = "live_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MandiPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String mandiName;

    @Column(nullable = false)
    private String cropName;

    private String cropNameHi;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerQuintal;

    @Column(nullable = false)
    private LocalDate priceDate;

    @Enumerated(EnumType.STRING)
    private PriceTrend trend;
}
