package com.farmeasy.irrigation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "irrigation_schedules", schema = "irrigation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IrrigationSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String cropType;
    private LocalTime startTime;
    private int durationMinutes;

    @Builder.Default
    private boolean active = true;

    private boolean autoMode;
}
