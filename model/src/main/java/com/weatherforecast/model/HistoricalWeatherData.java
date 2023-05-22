package com.weatherforecast.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class HistoricalWeatherData {
    LocalDate date;
    Map<LocalTime, Double> temperatures;
    Double highestTemperature;
    Double lowestTemperature;
}
