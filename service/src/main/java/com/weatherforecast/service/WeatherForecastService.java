package com.weatherforecast.service;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import com.weatherforecast.model.SevereWeatherWarning;
import com.weatherforecast.service.controllers.responses.HourlyForecastResponse;
import com.weatherforecast.service.exceptions.NoResponseFromAPIException;
import com.weatherforecast.service.openmeteo.WeatherForecastResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.drools.core.ClockType;
import org.drools.core.time.SessionPseudoClock;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastService {
    private final RestTemplate restTemplate;
    private final KieContainer kieContainer;
//    private final KieSession kieSession;

    @Value("${open-meteo.url.current-day}")
    String currentDayUrl;

    private static final String WARNINGS_CEP_SESSION_NAME = "warningCEPSession";

    public List<HourlyForecastResponse> getForecast() {
        return new ArrayList<>();
    }

    public List<String> getWarnings() {
        WeatherForecastResponse response = restTemplate.getForObject(currentDayUrl, WeatherForecastResponse.class);
        if (response == null) throw new NoResponseFromAPIException("Forecast data from Open-Meteo API is null.");

        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSessionConfiguration kieConfiguration = kieServices.newKieSessionConfiguration();
        kieConfiguration.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
        KieSession kieSession = kieContainer.newKieSession(WARNINGS_CEP_SESSION_NAME, kieConfiguration);
        SessionPseudoClock clock = kieSession.getSessionClock();

        List<LocalDateTime> allHours = response.getHourly().getTime();
        List<LocalDateTime> lastThreeHours = allHours.subList(allHours.size() - 3, allHours.size());
        List<Double> temperatures = response.getHourly().getTemperature_2m().subList(allHours.size() - 3, allHours.size());
        List<Integer> humidity = response.getHourly().getRelativehumidity_2m().subList(allHours.size() - 3, allHours.size());
        List<Double> precipitation = response.getHourly().getPrecipitation().subList(allHours.size() - 3, allHours.size());
        List<Integer> cloudCover = response.getHourly().getCloudcover().subList(allHours.size() - 3, allHours.size());
        List<Double> windSpeed = response.getHourly().getWindspeed_10m().subList(allHours.size() - 3, allHours.size());

        for (int i = 0; i < lastThreeHours.size(); i++) {
//            MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(lastThreeHours.get(i), 36.0, 50, 12.0, 50, 31.1);
            MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(lastThreeHours.get(i), temperatures.get(i), humidity.get(i), precipitation.get(i), cloudCover.get(i), windSpeed.get(i));
            System.out.println(m);
            kieSession.insert(m);
            kieSession.fireAllRules();
            clock.advanceTime(1, TimeUnit.HOURS);
        }

        List<String> warningMessages = new ArrayList<>();
        Collection<?> severeWeatherWarnings = kieSession.getObjects(obj -> obj instanceof SevereWeatherWarning);
        for (Object warning : severeWeatherWarnings) {
            SevereWeatherWarning severeWeatherWarning = (SevereWeatherWarning) warning;
            warningMessages.add(severeWeatherWarning.getMessage());
        }
        return warningMessages;
    }
}
