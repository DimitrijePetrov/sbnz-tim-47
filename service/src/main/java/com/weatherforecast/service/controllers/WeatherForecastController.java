package com.weatherforecast.service.controllers;

import com.weatherforecast.service.WeatherForecastService;
import com.weatherforecast.service.controllers.responses.HourlyForecastResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "api")
@CrossOrigin
@RequiredArgsConstructor
public class WeatherForecastController {
    private final WeatherForecastService wfService;

    @GetMapping(value = "/forecast")
    public ResponseEntity<List<HourlyForecastResponse>> getForecast() throws FileNotFoundException {
        return new ResponseEntity<>(wfService.getForecast(), HttpStatus.OK);
    }

    @GetMapping(value = "/warnings")
    public ResponseEntity<List<String>> getWarnings() {
        return new ResponseEntity<>(wfService.getWarnings(), HttpStatus.OK);
    }
}
