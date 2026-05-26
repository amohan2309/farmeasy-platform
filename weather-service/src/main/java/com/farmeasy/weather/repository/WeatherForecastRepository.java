package com.farmeasy.weather.repository;

import com.farmeasy.weather.entity.WeatherForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, java.util.UUID> {
    List<WeatherForecast> findByRegionOrderByForecastDateAsc(String region);
}
