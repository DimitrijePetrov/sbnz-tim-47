package com.weatherforecast.service;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import com.weatherforecast.service.controllers.HourlyTemperatureRequest;
import com.weatherforecast.service.exceptions.BadRequestException;
import com.weatherforecast.service.openmeteo.WeatherForecastResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastService {

    private final RestTemplate restTemplate;
    private final KieSession kieSession;

    @Value("${open-meteo.url.current-day}")
    String currentDayUrl;

//    public Boolean printTemperaturesByTime(HourlyTemperatureRequest request) {
//        List<LocalDateTime> times = request.getTime();
//        List<Double> temperatures = request.getTemperature_2m();
//
//        if (times.size() != temperatures.size()) throw new BadRequestException("Time and temperature lists must be of equal size!");
//
//        for (int i = 0; i < times.size(); i++) {
//            System.out.println(
//                    "Date: " + times.get(i).toLocalDate() +
//                    "Time: " + times.get(i).toLocalTime() +
//                    ", Temperature: " + temperatures.get(i)
//            );
//        }
//
//        return true;
//    }

    @Scheduled(fixedRate = 10000)
    public void simulateHourlySensorReading() {
//        WeatherForecastResponse response = restTemplate.getForObject(currentDayUrl, WeatherForecastResponse.class);
//        assert response != null;
//
//        List<LocalDateTime> times = response.getHourly().getTime();
//        List<Double> temperatures = response.getHourly().getTemperature_2m();
//
//        for (int i = 0; i < times.size(); i++) {
//            if (LocalDateTime.now().getHour() == times.get(i).getHour()) {
//                MeasuredHourlyWeatherData data = new MeasuredHourlyWeatherData(times.get(i), temperatures.get(i));
//                kieSession.insert(data);
//                int fired = kieSession.fireAllRules();
//                System.out.println("Fired " + fired + " rules.");
//                break;
//            }
//        }
        System.out.println(LocalDateTime.now().plusHours(10).minusDays(1).toLocalDate());
    }
}
