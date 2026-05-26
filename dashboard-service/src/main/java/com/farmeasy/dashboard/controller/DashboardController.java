package com.farmeasy.dashboard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final RestClient rest;

    public DashboardController(
            @Value("${farmeasy.marketplace-url:http://localhost:8086}") String marketplaceUrl,
            @Value("${farmeasy.weather-url:http://localhost:8087}") String weatherUrl,
            @Value("${farmeasy.iot-url:http://localhost:8089}") String iotUrl,
            @Value("${farmeasy.prices-url:http://localhost:8091}") String pricesUrl,
            @Value("${farmeasy.crop-selling-url:http://localhost:8090}") String cropSellingUrl) {
        this.rest = RestClient.builder().build();
        this.marketplaceUrl = marketplaceUrl;
        this.weatherUrl = weatherUrl;
        this.iotUrl = iotUrl;
        this.pricesUrl = pricesUrl;
        this.cropSellingUrl = cropSellingUrl;
    }

    private final String marketplaceUrl;
    private final String weatherUrl;
    private final String iotUrl;
    private final String pricesUrl;
    private final String cropSellingUrl;

    @GetMapping("/home")
    public Map<String, Object> home(@RequestHeader(value = "X-User-Id", required = false) String userId) {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("userId", userId);
        dashboard.put("greeting", "Welcome to Farm Easy Portal");
        dashboard.put("profitSummary", Map.of(
                "seasonRevenueInr", 185000,
                "seasonExpensesInr", 92000,
                "estimatedProfitInr", 93000,
                "profitChangePercent", 12
        ));
        dashboard.put("quickStats", Map.of(
                "activeListings", safeCount(cropSellingUrl + "/api/crop-selling/listings"),
                "mandiPricesToday", safeCount(pricesUrl + "/api/prices/mandi"),
                "smartChipOnline", true,
                "waterSavedPercent", 18
        ));
        dashboard.put("weather", safeList(weatherUrl + "/api/weather/forecast?region=Punjab"));
        dashboard.put("topMandiPrices", safeList(pricesUrl + "/api/prices/mandi"));
        dashboard.put("iotDevices", safeList(iotUrl + "/api/iot/devices"));
        dashboard.put("marketplaceHighlights", safeList(marketplaceUrl + "/api/marketplace/products"));
        return dashboard;
    }

    @SuppressWarnings("unchecked")
    private List<Object> safeList(String url) {
        try {
            Object body = rest.get().uri(url).retrieve().body(Object.class);
            if (body instanceof List<?> list) return (List<Object>) list;
            if (body instanceof Object[] arr) return Arrays.asList(arr);
        } catch (Exception ignored) {
        }
        return List.of();
    }

    private int safeCount(String url) {
        return safeList(url).size();
    }
}
