package com.farmeasy.catalog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "farm_apps", schema = "catalog")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmApp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    private String titleEn;
    private String titleHi;
    private String descriptionEn;
    private String iconUrl;
    private int sortOrder;
    private boolean active;
}
