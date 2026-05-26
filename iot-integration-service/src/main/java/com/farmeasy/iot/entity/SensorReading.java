package com.farmeasy.iot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sensor_readings", schema = "iot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorReading {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String chipId;

    private double soilMoisturePercent;
    private double soilPh;
    private double npkN;
    private double npkP;
    private double npkK;
    private double temperatureCelsius;
    private double humidityPercent;

    @Column(nullable = false)
    private Instant recordedAt;
}
