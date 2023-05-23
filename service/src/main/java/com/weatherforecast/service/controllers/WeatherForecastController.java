package com.weatherforecast.service.controllers;

import com.weatherforecast.service.WeatherForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api")
@CrossOrigin
@RequiredArgsConstructor
public class WeatherForecastController {
    private final WeatherForecastService wfService;

    @GetMapping
    public ResponseEntity<Void> getTemperature() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
