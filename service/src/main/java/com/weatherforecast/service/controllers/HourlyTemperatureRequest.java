package com.weatherforecast.service.controllers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HourlyTemperatureRequest {
    @NotNull(message = "A time list is needed!")
    @NotEmpty(message = "The time list must not be empty!")
    List<LocalDateTime> time;

    @NotNull(message = "A temperature_2m list is needed!")
    @NotEmpty(message = "The temperature_2m list must not be empty!")
    List<Double> temperature_2m;
}
