package com.weatherforecast.service.controllers.responses;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HourlyForecastResponse {
    Integer time;
    Double temperature;
    Integer precipitation;

    public HourlyForecastResponse(Integer time, Double temperature, Integer precipitation) {
        this.time = time;
        this.temperature = temperature;
        this.precipitation = precipitation;
    }
}
