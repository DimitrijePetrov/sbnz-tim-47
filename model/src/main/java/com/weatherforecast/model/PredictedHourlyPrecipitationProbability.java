package com.weatherforecast.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class PredictedHourlyPrecipitationProbability {
    LocalDate date;
    Integer hour;
    Integer precipitationProbability;
    Boolean modifiersApplied;

    public PredictedHourlyPrecipitationProbability(LocalDateTime dateTime) {
        this.date = dateTime.toLocalDate();
        this.hour = dateTime.toLocalTime().getHour();
        this.precipitationProbability = null;
        this.modifiersApplied = false;
    }

    public PredictedHourlyPrecipitationProbability(LocalDateTime dateTime, Integer precipitationProbability) {
        this.date = dateTime.toLocalDate();
        this.hour = dateTime.toLocalTime().getHour();
        this.precipitationProbability = precipitationProbability;
        this.modifiersApplied = false;
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

    public Integer getPrecipitationProbability() {
        return precipitationProbability;
    }

    public void setPrecipitationProbability(Integer precipitationProbability) {
        this.precipitationProbability = precipitationProbability;
    }

    public Boolean getModifiersApplied() {
        return modifiersApplied;
    }

    public void setModifiersApplied(Boolean modifiersApplied) {
        this.modifiersApplied = modifiersApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PredictedHourlyPrecipitationProbability that = (PredictedHourlyPrecipitationProbability) o;
        return Objects.equals(date, that.date) && Objects.equals(hour, that.hour) && Objects.equals(precipitationProbability, that.precipitationProbability) && Objects.equals(modifiersApplied, that.modifiersApplied);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, hour, precipitationProbability, modifiersApplied);
    }

    @Override
    public String toString() {
        return "PredictedHourlyPrecipitationProbability{" +
                "date=" + date +
                ", hour=" + hour +
                ", precipitationProbability=" + precipitationProbability +
                ", modifiersApplied=" + modifiersApplied +
                '}';
    }

    public void addHumidityModifier(Integer humidity) {
        int maxModifier = 25;
        int minModifier = 0;
        int maxHumidity = 60;
        int minHumidity = 20;

        int humidityModifier;
        if (humidity <= minHumidity) {
            humidityModifier = minModifier;
        } else if (humidity >= maxHumidity) {
            humidityModifier = maxModifier;
        } else {
            humidityModifier = (((humidity - minHumidity) * (maxModifier - minModifier)) / (maxHumidity - minHumidity)) + minModifier;
        }
        this.precipitationProbability += humidityModifier;
    }

    public void addCloudCoverModifier(Integer cloudCover) {
        int cloudCoverModifier = cloudCover / 4;
        this.precipitationProbability += cloudCoverModifier;
    }

    public void applyWindSpeedFactor(Double windSpeed) {
        LocalDateTime dateTimeOfPrediction = LocalDateTime.of(this.date, LocalTime.of(this.hour, 0));
        long hoursDifference = ChronoUnit.HOURS.between(LocalDateTime.now(), dateTimeOfPrediction) + 1;
        double modifierPerDay = Math.pow(Math.E, (-0.01 * windSpeed));
        this.precipitationProbability = (int) (this.precipitationProbability * Math.pow(modifierPerDay, hoursDifference));
    }
}
