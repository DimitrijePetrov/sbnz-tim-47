package com.weatherforecast.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api")
@CrossOrigin
@RequiredArgsConstructor
public class WeatherForecastController {
    private final WeatherForecastService wfService;

    @GetMapping
    public ResponseEntity<Boolean> getResponse() {
        Boolean responseBody = wfService.getBooleanValue();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
