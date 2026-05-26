package com.farmeasy.weather.config;

import com.farmeasy.weather.entity.WeatherForecast;
import com.farmeasy.weather.repository.WeatherForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class WeatherDataSeeder implements CommandLineRunner {
    private final WeatherForecastRepository forecastRepo;

    @Override
    public void run(String... args) {
        if (forecastRepo.count() > 0) return;
        String region = "Punjab";
        for (int i = 0; i < 5; i++) {
            LocalDate d = LocalDate.now().plusDays(i);
            forecastRepo.save(WeatherForecast.builder()
                    .region(region)
                    .forecastDate(d)
                    .tempCelsiusMax(34 - i)
                    .tempCelsiusMin(22 + i)
                    .humidityPercent(65 + i * 2)
                    .rainChancePercent(i == 2 ? 70 : 15 + i * 5)
                    .alertMessage(i == 2 ? "Heavy rain expected — delay pesticide spray" : null)
                    .build());
        }
    }
}
