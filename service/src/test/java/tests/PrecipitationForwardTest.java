package tests;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;

public class PrecipitationForwardTest {
    protected final String ksessionName = "precipitationForwardSession";

    @Test
    public void testPrecipitationForwardWithNoPrecipitationInThePastHour() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        KieSession kSession = kieContainer.newKieSession(ksessionName);

        LocalDateTime now = LocalDateTime.now();

        MeasuredHourlyWeatherData m1 = new MeasuredHourlyWeatherData(now, 24.0, 30,0.0, 100, 1.1);

        kSession.insert(m1);
        int firedRules = kSession.fireAllRules();
        MatcherAssert.assertThat(firedRules, equalTo(4));
    }

    @Test
    public void testPrecipitationForwardWithPrecipitationInThePastHour() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.getKieClasspathContainer();
        KieSession kSession = kieContainer.newKieSession(ksessionName);

        LocalDateTime now = LocalDateTime.now();

        MeasuredHourlyWeatherData m1 = new MeasuredHourlyWeatherData(now, 24.0, 60, 10.0, 100, 30.0);

        kSession.insert(m1);
        int firedRules = kSession.fireAllRules();
        MatcherAssert.assertThat(firedRules, equalTo(4));
    }
}
