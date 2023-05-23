package com.weatherforecast.service.openmeteo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HourlyData {
    List<LocalDateTime> time;
    List<Double> temperature_2m;
}
