package com.farmeasy.weather.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.farmeasy.weather.dto.HourlyForecastResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class OpenMeteoClient {
    static final List<String> HOURLY_VARIABLES = List.of(
            "temperature_2m",
            "relative_humidity_2m",
            "dew_point_2m",
            "apparent_temperature",
            "precipitation",
            "rain",
            "showers",
            "soil_temperature_0cm",
            "soil_temperature_6cm",
            "soil_temperature_18cm",
            "soil_temperature_54cm",
            "soil_moisture_0_to_1cm",
            "soil_moisture_1_to_3cm",
            "soil_moisture_3_to_9cm",
            "soil_moisture_9_to_27cm",
            "soil_moisture_27_to_81cm",
            "temperature_80m",
            "temperature_120m",
            "temperature_180m",
            "wind_speed_10m",
            "wind_speed_80m",
            "wind_speed_120m",
            "wind_speed_180m",
            "wind_direction_10m",
            "wind_direction_80m",
            "wind_direction_120m",
            "wind_direction_180m",
            "wind_gusts_10m",
            "et0_fao_evapotranspiration",
            "evapotranspiration",
            "visibility",
            "weather_code"
    );

    private static final Map<String, String> FIELD_NAMES = Map.ofEntries(
            Map.entry("temperature_2m", "temperature2m"),
            Map.entry("relative_humidity_2m", "relativeHumidity2m"),
            Map.entry("dew_point_2m", "dewPoint2m"),
            Map.entry("apparent_temperature", "apparentTemperature"),
            Map.entry("precipitation", "precipitation"),
            Map.entry("rain", "rain"),
            Map.entry("showers", "showers"),
            Map.entry("soil_temperature_0cm", "soilTemperature0cm"),
            Map.entry("soil_temperature_6cm", "soilTemperature6cm"),
            Map.entry("soil_temperature_18cm", "soilTemperature18cm"),
            Map.entry("soil_temperature_54cm", "soilTemperature54cm"),
            Map.entry("soil_moisture_0_to_1cm", "soilMoisture0To1cm"),
            Map.entry("soil_moisture_1_to_3cm", "soilMoisture1To3cm"),
            Map.entry("soil_moisture_3_to_9cm", "soilMoisture3To9cm"),
            Map.entry("soil_moisture_9_to_27cm", "soilMoisture9To27cm"),
            Map.entry("soil_moisture_27_to_81cm", "soilMoisture27To81cm"),
            Map.entry("temperature_80m", "temperature80m"),
            Map.entry("temperature_120m", "temperature120m"),
            Map.entry("temperature_180m", "temperature180m"),
            Map.entry("wind_speed_10m", "windSpeed10m"),
            Map.entry("wind_speed_80m", "windSpeed80m"),
            Map.entry("wind_speed_120m", "windSpeed120m"),
            Map.entry("wind_speed_180m", "windSpeed180m"),
            Map.entry("wind_direction_10m", "windDirection10m"),
            Map.entry("wind_direction_80m", "windDirection80m"),
            Map.entry("wind_direction_120m", "windDirection120m"),
            Map.entry("wind_direction_180m", "windDirection180m"),
            Map.entry("wind_gusts_10m", "windGusts10m"),
            Map.entry("et0_fao_evapotranspiration", "et0FaoEvapotranspiration"),
            Map.entry("evapotranspiration", "evapotranspiration"),
            Map.entry("visibility", "visibility"),
            Map.entry("weather_code", "weatherCode")
    );

    private final RestClient rest;
    private final String forecastUrl;
    private final double defaultLatitude;
    private final double defaultLongitude;
    private final String defaultTimezone;
    private final String defaultLocationName;

    public OpenMeteoClient(
            @Value("${farmeasy.weather.open-meteo-url:https://api.open-meteo.com/v1/forecast}") String forecastUrl,
            @Value("${farmeasy.weather.latitude:25.5941}") double defaultLatitude,
            @Value("${farmeasy.weather.longitude:85.1356}") double defaultLongitude,
            @Value("${farmeasy.weather.timezone:auto}") String defaultTimezone,
            @Value("${farmeasy.weather.location-name:Patna, India}") String defaultLocationName) {
        this.rest = RestClient.builder().build();
        this.forecastUrl = forecastUrl;
        this.defaultLatitude = defaultLatitude;
        this.defaultLongitude = defaultLongitude;
        this.defaultTimezone = defaultTimezone;
        this.defaultLocationName = defaultLocationName;
    }

    public double getDefaultLatitude() {
        return defaultLatitude;
    }

    public double getDefaultLongitude() {
        return defaultLongitude;
    }

    public String getDefaultTimezone() {
        return defaultTimezone;
    }

    public String getDefaultLocationName() {
        return defaultLocationName;
    }

    public HourlyForecastResponse fetchHourly(Double latitude, Double longitude, String timezone) {
        double lat = latitude != null ? latitude : defaultLatitude;
        double lon = longitude != null ? longitude : defaultLongitude;
        String tz = (timezone == null || timezone.isBlank()) ? defaultTimezone : timezone;

        URI uri = UriComponentsBuilder.fromUriString(forecastUrl)
                .queryParam("latitude", lat)
                .queryParam("longitude", lon)
                .queryParam("hourly", String.join(",", HOURLY_VARIABLES))
                .queryParam("timezone", tz)
                .encode()
                .build()
                .toUri();

        JsonNode root;
        try {
            root = rest.get().uri(uri).retrieve().body(JsonNode.class);
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Weather forecast unavailable", ex);
        }
        if (root == null || root.path("hourly").path("time").isMissingNode()) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Weather forecast unavailable");
        }

        JsonNode hourly = root.path("hourly");
        JsonNode times = hourly.path("time");
        JsonNode unitNode = root.path("hourly_units");

        Map<String, String> units = new LinkedHashMap<>();
        for (var entry : FIELD_NAMES.entrySet()) {
            JsonNode unit = unitNode.path(entry.getKey());
            if (!unit.isMissingNode() && !unit.isNull()) {
                units.put(entry.getValue(), unit.asText());
            }
        }

        List<Map<String, Object>> hours = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("time", times.get(i).asText());
            for (var entry : FIELD_NAMES.entrySet()) {
                row.put(entry.getValue(), numericOrNull(hourly.path(entry.getKey()), i));
            }
            hours.add(row);
        }

        return HourlyForecastResponse.builder()
                .latitude(root.path("latitude").asDouble(lat))
                .longitude(root.path("longitude").asDouble(lon))
                .timezone(root.path("timezone").asText(tz))
                .timezoneAbbreviation(root.path("timezone_abbreviation").asText(""))
                .locationName(defaultLocationName)
                .units(units)
                .hours(hours)
                .build();
    }

    private static Double numericOrNull(JsonNode array, int index) {
        if (array == null || array.isMissingNode() || index >= array.size()) {
            return null;
        }
        JsonNode value = array.get(index);
        if (value == null || value.isNull()) {
            return null;
        }
        return value.asDouble();
    }
}
