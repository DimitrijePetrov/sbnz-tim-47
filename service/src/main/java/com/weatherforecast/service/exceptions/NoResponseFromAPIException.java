package com.weatherforecast.service.exceptions;

public class NoResponseFromAPIException extends RuntimeException {
    public NoResponseFromAPIException(String message) {
        super(message);
    }
}
