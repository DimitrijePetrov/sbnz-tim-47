package com.weatherforecast.service.openmeteo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class WeatherForecastResponse {
    @NotNull(message = "Hourly data is null")
    @NotEmpty(message = "Hourly data is empty")
    HourlyData hourly;
}
