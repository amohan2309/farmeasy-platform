package com.farmeasy.weather.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "forecasts", schema = "weather")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private LocalDate forecastDate;

    private double tempCelsiusMax;
    private double tempCelsiusMin;
    private int humidityPercent;
    private int rainChancePercent;
    private String alertMessage;
}
