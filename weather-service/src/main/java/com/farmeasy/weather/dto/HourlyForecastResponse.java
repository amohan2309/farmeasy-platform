package com.farmeasy.weather.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class HourlyForecastResponse {
    double latitude;
    double longitude;
    String timezone;
    String timezoneAbbreviation;
    String locationName;
    Map<String, String> units;
    List<Map<String, Object>> hours;
}
