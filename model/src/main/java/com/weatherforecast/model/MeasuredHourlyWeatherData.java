package com.weatherforecast.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class MeasuredHourlyWeatherData {
    LocalDate date;
    Integer hour;
    Double temperature;

    public MeasuredHourlyWeatherData(LocalDateTime dateTime, Double temperature) {
        this.date = dateTime.toLocalDate();
        this.hour = dateTime.toLocalTime().getHour();
        this.temperature = temperature;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasuredHourlyWeatherData that = (MeasuredHourlyWeatherData) o;
        return Objects.equals(date, that.date) && Objects.equals(hour, that.hour) && Objects.equals(temperature, that.temperature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, hour, temperature);
    }
}
