package com.weatherforecast.service;

import com.weatherforecast.service.controllers.HourlyTemperatureRequest;
import com.weatherforecast.service.exceptions.BadRequestException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastService {

    public Boolean printTemperaturesByTime(HourlyTemperatureRequest request) {
        List<LocalDateTime> times = request.getTime();
        List<Double> temperatures = request.getTemperature_2m();

        if (times.size() != temperatures.size()) throw new BadRequestException("Time and temperature lists must be of equal size!");

        for (int i = 0; i < times.size(); i++) {
            System.out.println(
                    "Date: " + times.get(i).toLocalDate() +
                    "Time: " + times.get(i).toLocalTime() +
                    ", Temperature: " + temperatures.get(i)
            );
        }

        return true;
    }
}
