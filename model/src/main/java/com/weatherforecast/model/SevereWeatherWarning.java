package com.weatherforecast.model;

import java.util.Objects;

public class SevereWeatherWarning {
    String message;

    public SevereWeatherWarning(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SevereWeatherWarning that = (SevereWeatherWarning) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
}
