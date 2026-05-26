package com.farmeasy.weather.controller;

import com.farmeasy.weather.entity.WeatherForecast;
import com.farmeasy.weather.repository.WeatherForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherForecastRepository forecastRepo;

    @GetMapping("/forecast")
    public List<WeatherForecast> forecast(@RequestParam(defaultValue = "Punjab") String region) {
        return forecastRepo.findByRegionOrderByForecastDateAsc(region);
    }

    @GetMapping("/alerts")
    public List<WeatherForecast> alerts(@RequestParam(defaultValue = "Punjab") String region) {
        return forecastRepo.findByRegionOrderByForecastDateAsc(region).stream()
                .filter(f -> f.getAlertMessage() != null && !f.getAlertMessage().isBlank())
                .toList();
    }
}
