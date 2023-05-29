package com.weatherforecast.service;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import com.weatherforecast.model.PredictedHourlyPrecipitationProbability;
import com.weatherforecast.model.PredictedHourlyTemperature;
import com.weatherforecast.model.SevereWeatherWarning;
import com.weatherforecast.service.controllers.responses.HourlyForecastResponse;
import com.weatherforecast.service.exceptions.NoResponseFromAPIException;
import com.weatherforecast.service.openmeteo.WeatherForecastResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.drools.core.ClockType;
import org.drools.core.time.SessionPseudoClock;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.utils.KieHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherForecastService {
    private final RestTemplate restTemplate;
    private final KieContainer kieContainer;

    @Value("${open-meteo.url.current-day}")
    String currentDayUrl;

    private static final String TEMPERATURE_TEMPLATE_FILE_PATH = "\\kjar\\src\\main\\resources\\rules\\template\\temperatureTemplate.drt";
    private static final String PRECIPITATION_FORWARD_SESSION_NAME = "precipitationForwardSession";
    private static final String WARNINGS_CEP_SESSION_NAME = "warningCEPSession";

    public List<HourlyForecastResponse> getForecast() throws FileNotFoundException {
        WeatherForecastResponse response = restTemplate.getForObject(currentDayUrl, WeatherForecastResponse.class);
        if (response == null) throw new NoResponseFromAPIException("Forecast data from Open-Meteo API is null.");

        KieSession temperatureTemplateKieSession = createTemperatureTemplateKieSession(LocalDateTime.now());
        temperatureTemplateKieSession.fireAllRules();
        KieSession precipitationKieSession = kieContainer.newKieSession(PRECIPITATION_FORWARD_SESSION_NAME);
        precipitationKieSession.fireAllRules();

        LocalDateTime currentDateAndHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        for (int d = 2; d >= 0; d--) {
            for (int h = 23; h >= 0; h--) {
                int index = response.getHourly().getTime().lastIndexOf(currentDateAndHour.minusDays(d).minusHours(h));

                LocalDateTime dateTime = response.getHourly().getTime().get(index);
                Double temperature = response.getHourly().getTemperature_2m().get(index);
                Integer humidity = response.getHourly().getRelativehumidity_2m().get(index);
                Double precipitation = response.getHourly().getPrecipitation().get(index);
                Integer cloudCover = response.getHourly().getCloudcover().get(index);
                Double windSpeed = response.getHourly().getWindspeed_10m().get(index);

                MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(dateTime, temperature, humidity, precipitation, cloudCover, windSpeed);
                System.out.println(m);
                temperatureTemplateKieSession.insert(m);
                temperatureTemplateKieSession.fireAllRules();
            }
        }

        List<Integer> hours = get24HoursFromNow();
        System.out.println(hours);

        List<PredictedHourlyTemperature> predictedHourlyTemperatures = new ArrayList<>();
        Collection<?> objects = temperatureTemplateKieSession.getObjects(obj -> obj instanceof PredictedHourlyTemperature);
        for (Object o : objects) {
            predictedHourlyTemperatures.add((PredictedHourlyTemperature) o);
        }
        predictedHourlyTemperatures.sort(Comparator.comparingInt(t -> hours.indexOf(t.getHour())));
        System.out.println(predictedHourlyTemperatures);

        int index = response.getHourly().getTime().lastIndexOf(currentDateAndHour);

        LocalDateTime dateTime = response.getHourly().getTime().get(index);
        Double temperature = response.getHourly().getTemperature_2m().get(index);
        Integer humidity = response.getHourly().getRelativehumidity_2m().get(index);
        Double precipitation = response.getHourly().getPrecipitation().get(index);
        Integer cloudCover = response.getHourly().getCloudcover().get(index);
        Double windSpeed = response.getHourly().getWindspeed_10m().get(index);

        MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(dateTime, temperature, humidity, precipitation, cloudCover, windSpeed);
        System.out.println(m);
        precipitationKieSession.insert(m);
        precipitationKieSession.fireAllRules();

        List<PredictedHourlyPrecipitationProbability> predictedHourlyPrecipitationProbabilities = new ArrayList<>();
        objects = precipitationKieSession.getObjects(obj -> obj instanceof PredictedHourlyPrecipitationProbability);
        for (Object o : objects) {
            predictedHourlyPrecipitationProbabilities.add((PredictedHourlyPrecipitationProbability) o);
        }
        predictedHourlyPrecipitationProbabilities.sort(Comparator.comparingInt(t -> hours.indexOf(t.getHour())));

        List<HourlyForecastResponse> forecast = new ArrayList<>();
        for (int i = 0; i < hours.size(); i++) {
            Integer hour = hours.get(i);
            Optional<PredictedHourlyTemperature> predictedHourlyTemperature = predictedHourlyTemperatures.stream().filter(obj -> Objects.equals(obj.getHour(), hour)).findFirst();
            Double t = predictedHourlyTemperature.get().getTemperature();
            forecast.add(new HourlyForecastResponse(hour, t, predictedHourlyPrecipitationProbabilities.get(0).getPrecipitationProbability()));
        }
        return forecast;
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

    private KieSession createTemperatureTemplateKieSession(LocalDateTime startingDateTime) throws FileNotFoundException {
        ApplicationHome home = new ApplicationHome(WeatherForecastService.class);
        String path = home.getDir().getParentFile().getParentFile().getParent().concat(TEMPERATURE_TEMPLATE_FILE_PATH);
        InputStream template = new FileInputStream(path);

        String y = String.valueOf(startingDateTime.getYear());
        String m = String.valueOf(startingDateTime.getMonthValue());
        String d = String.valueOf(startingDateTime.getDayOfMonth());
        String h = String.valueOf(startingDateTime.getHour());
        String min = String.valueOf(startingDateTime.getMinute());

        String[][] hours = new String[24][2];
        for (int i = 0; i <= 23; i++) {
            String hourString = String.valueOf(i + 1);
            hours[i] = new String[] {hourString, y, m, d, h, min};
        }

        DataProvider dataProvider = new ArrayDataProvider(hours);
        DataProviderCompiler converter = new DataProviderCompiler();
        String drl = converter.compile(dataProvider, template);

        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

        Results results = kieHelper.verify();

        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: "+message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper.build().newKieSession();
    }

    private List<Integer> get24HoursFromNow() {
        LocalDateTime currentDateAndHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        List<Integer> hours = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            hours.add(currentDateAndHour.plusHours(i).getHour());
        }
        return hours;
    }
}
