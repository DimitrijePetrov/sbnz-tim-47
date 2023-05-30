package com.weatherforecast.service;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import com.weatherforecast.model.PredictedHourlyPrecipitationProbability;
import com.weatherforecast.model.PredictedHourlyTemperature;
import com.weatherforecast.model.SevereWeatherWarning;
import com.weatherforecast.service.controllers.responses.HourlyForecastResponse;
import com.weatherforecast.service.exceptions.NoResponseFromAPIException;
import com.weatherforecast.service.openmeteo.HourlyData;
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
    private static final String PRECIPITATION_TEMPLATE_FILE_PATH = "\\kjar\\src\\main\\resources\\rules\\template\\precipitationTemplate.drt";
//    private static final String PRECIPITATION_FORWARD_SESSION_NAME = "precipitationForwardSession";
    private static final String WARNINGS_CEP_SESSION_NAME = "warningCEPSession";

    public List<HourlyForecastResponse> getForecast() throws FileNotFoundException {
        WeatherForecastResponse response = restTemplate.getForObject(currentDayUrl, WeatherForecastResponse.class);
        if (response == null) throw new NoResponseFromAPIException("Forecast data from Open-Meteo API is null.");

        List<PredictedHourlyTemperature> predictedHourlyTemperatures = getPredictedHourlyTemperatures(response.getHourly());
        List<PredictedHourlyPrecipitationProbability> predictedHourlyPrecipitationProbabilities = getPredictedHourlyPrecipitationProbability(response.getHourly());

        List<Integer> hours = get24HoursFromNow();
        predictedHourlyTemperatures.sort(Comparator.comparingInt(t -> hours.indexOf(t.getHour())));
        predictedHourlyPrecipitationProbabilities.sort(Comparator.comparingInt(p -> hours.indexOf(p.getHour())));

        List<HourlyForecastResponse> forecast = new ArrayList<>();
        for (Integer hour : hours) {
            Optional<PredictedHourlyTemperature> optionalTemperature = predictedHourlyTemperatures.stream().filter(obj -> Objects.equals(obj.getHour(), hour)).findFirst();
            Double temperature = optionalTemperature.get().getTemperature();

            Optional<PredictedHourlyPrecipitationProbability> optionalPrecipitation = predictedHourlyPrecipitationProbabilities.stream().filter(obj -> Objects.equals(obj.getHour(), hour)).findFirst();
            Integer precipitation = optionalPrecipitation.get().getPrecipitationProbability();

            forecast.add(new HourlyForecastResponse(hour, temperature, precipitation));
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
            MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(lastThreeHours.get(i), 36.0, 50, 12.0, 50, 31.1);
//            MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(lastThreeHours.get(i), temperatures.get(i), humidity.get(i), precipitation.get(i), cloudCover.get(i), windSpeed.get(i));
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

    private List<PredictedHourlyTemperature> getPredictedHourlyTemperatures(HourlyData hourlyData) throws FileNotFoundException {
        KieSession temperatureTemplateKieSession = createTemperatureTemplateKieSession(TEMPERATURE_TEMPLATE_FILE_PATH, LocalDateTime.now());
        temperatureTemplateKieSession.fireAllRules();

        LocalDateTime currentDateAndHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        for (int d = 2; d >= 0; d--) {
            for (int h = 23; h >= 0; h--) {
                int index = hourlyData.getTime().lastIndexOf(currentDateAndHour.minusDays(d).minusHours(h));
                MeasuredHourlyWeatherData m = createMeasuredHourlyWeatherData(hourlyData, index);
                temperatureTemplateKieSession.insert(m);
                temperatureTemplateKieSession.fireAllRules();
            }
        }

        List<PredictedHourlyTemperature> predictedHourlyTemperatures = new ArrayList<>();
        Collection<?> objects = temperatureTemplateKieSession.getObjects(obj -> obj instanceof PredictedHourlyTemperature);
        for (Object o : objects) {
            predictedHourlyTemperatures.add((PredictedHourlyTemperature) o);
        }
        return predictedHourlyTemperatures;
    }

    private List<PredictedHourlyPrecipitationProbability> getPredictedHourlyPrecipitationProbability(HourlyData hourlyData) throws FileNotFoundException {
        KieSession temperatureTemplateKieSession = createTemperatureTemplateKieSession(PRECIPITATION_TEMPLATE_FILE_PATH, LocalDateTime.now());
        temperatureTemplateKieSession.fireAllRules();

        LocalDateTime currentDateAndHour = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);

        int index = hourlyData.getTime().lastIndexOf(currentDateAndHour);
        MeasuredHourlyWeatherData m = createMeasuredHourlyWeatherData(hourlyData, index);
        temperatureTemplateKieSession.insert(m);
        temperatureTemplateKieSession.fireAllRules();

        List<PredictedHourlyPrecipitationProbability> predictedHourlyPrecipitationProbabilities = new ArrayList<>();
        Collection<?> objects = temperatureTemplateKieSession.getObjects(obj -> obj instanceof PredictedHourlyPrecipitationProbability);
        for (Object o : objects) {
            predictedHourlyPrecipitationProbabilities.add((PredictedHourlyPrecipitationProbability) o);
        }
        return predictedHourlyPrecipitationProbabilities;
    }

    private MeasuredHourlyWeatherData createMeasuredHourlyWeatherData(HourlyData hourlyData, int index) {
        LocalDateTime dateTime = hourlyData.getTime().get(index);
        Double temperature = hourlyData.getTemperature_2m().get(index);
        Integer humidity = hourlyData.getRelativehumidity_2m().get(index);
        Double precipitation = hourlyData.getPrecipitation().get(index);
        Integer cloudCover = hourlyData.getCloudcover().get(index);
        Double windSpeed = hourlyData.getWindspeed_10m().get(index);

        return new MeasuredHourlyWeatherData(dateTime, temperature, humidity, precipitation, cloudCover, windSpeed);
    }

    private KieSession createTemperatureTemplateKieSession(String templateFilePath, LocalDateTime startingDateTime) throws FileNotFoundException {
        ApplicationHome home = new ApplicationHome(WeatherForecastService.class);
        String path = home.getDir().getParentFile().getParentFile().getParent().concat(templateFilePath);
        InputStream template = new FileInputStream(path);

        String y = String.valueOf(startingDateTime.getYear());
        String m = String.valueOf(startingDateTime.getMonthValue());
        String d = String.valueOf(startingDateTime.getDayOfMonth());
        String h = String.valueOf(startingDateTime.getHour());
        String min = String.valueOf(startingDateTime.getMinute());

        String[][] hours = new String[24][5];
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
