package com.weatherforecast.service;

import com.weatherforecast.service.controllers.responses.HourlyForecastResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastService {
    private final RestTemplate restTemplate;
    private final KieSession kieSession;

    @Value("${open-meteo.url.current-day}")
    String currentDayUrl;

    public List<HourlyForecastResponse> getForecast() {
        return new ArrayList<>();
    }

    public List<String> getWarnings() {
        return new ArrayList<>();
    }
}
