package tests;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;

public class TemperatureForwardTest {
    protected final String ksessionName = "temperatureForwardSession";

    @Test
    public void testGetAverageTemperatureForThisHourInThePastThreeDays() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        KieSession kSession = kieContainer.newKieSession(ksessionName);

        LocalDateTime oneHourFromNow = LocalDateTime.parse("2023-05-29T20:13:24.089848700").plusHours(1);

        MeasuredHourlyWeatherData m1 = new MeasuredHourlyWeatherData(oneHourFromNow.minusDays(1), 24.0, 50, 11.0, 50, 1.1);
        MeasuredHourlyWeatherData m2 = new MeasuredHourlyWeatherData(oneHourFromNow.minusDays(2), 22.0, 50, 0.0, 50, 1.1);
        MeasuredHourlyWeatherData m3 = new MeasuredHourlyWeatherData(oneHourFromNow.minusDays(3), 20.0, 50, 11.2, 50, 1.1);

        kSession.insert(m1);
        kSession.insert(m2);
        kSession.insert(m3);
        int firedRules = kSession.fireAllRules();
        MatcherAssert.assertThat(firedRules, equalTo(4));
    }
}
