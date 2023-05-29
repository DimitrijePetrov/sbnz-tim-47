package com.weatherforecast.model;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Role(Role.Type.EVENT)
@Timestamp("executionTime")
@Expires("3d1m")
public class MeasuredHourlyWeatherData implements Serializable {
    Date executionTime;
    LocalDate date;
    Integer hour;
    Double temperature;
    Integer humidity;
    Double precipitation;
    Integer cloudCover;
    Double windSpeed;

    public MeasuredHourlyWeatherData(LocalDateTime dateTime, Double temperature, Integer humidity, Double precipitation, Integer cloudCover, Double windSpeed) {
        this.executionTime = new Date();
        this.date = dateTime.toLocalDate();
        this.hour = dateTime.toLocalTime().getHour();
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitation = precipitation;
        this.cloudCover = cloudCover;
        this.windSpeed = windSpeed;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
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

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public Integer getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(Integer cloudCover) {
        this.cloudCover = cloudCover;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasuredHourlyWeatherData that = (MeasuredHourlyWeatherData) o;
        return Objects.equals(date, that.date) && Objects.equals(hour, that.hour) && Objects.equals(temperature, that.temperature) && Objects.equals(humidity, that.humidity) && Objects.equals(precipitation, that.precipitation) && Objects.equals(cloudCover, that.cloudCover) && Objects.equals(windSpeed, that.windSpeed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, hour, temperature, humidity, precipitation, cloudCover, windSpeed);
    }
}
