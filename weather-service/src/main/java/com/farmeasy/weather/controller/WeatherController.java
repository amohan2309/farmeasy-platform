package com.farmeasy.weather.controller;

import com.farmeasy.weather.client.OpenMeteoClient;
import com.farmeasy.weather.dto.HourlyForecastResponse;
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
    private final OpenMeteoClient openMeteoClient;

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

    @GetMapping("/hourly")
    public HourlyForecastResponse hourly(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String timezone) {
        return openMeteoClient.fetchHourly(latitude, longitude, timezone);
    }
}
