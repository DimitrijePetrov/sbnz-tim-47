package com.weatherforecast.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

public class PredictedHourlyWeatherData {
    LocalDate date;
    Integer hour;
    Double temperature;
    Boolean precipitationApplied;

    public PredictedHourlyWeatherData(LocalDateTime dateTime, Double temperature) {
        this.date = dateTime.toLocalDate();
        this.hour = dateTime.toLocalTime().getHour();
        this.temperature = temperature;
        this.precipitationApplied = false;
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

    public Boolean getPrecipitationApplied() {
        return precipitationApplied;
    }

    public void setPrecipitationApplied(Boolean precipitationApplied) {
        this.precipitationApplied = precipitationApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredictedHourlyWeatherData that = (PredictedHourlyWeatherData) o;
        return Objects.equals(date, that.date) && Objects.equals(hour, that.hour) && Objects.equals(temperature, that.temperature) && Objects.equals(precipitationApplied, that.precipitationApplied);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, hour, temperature, precipitationApplied);
    }

    public void addTemperature(Double modifier) {
        this.temperature += modifier;
    }
}
