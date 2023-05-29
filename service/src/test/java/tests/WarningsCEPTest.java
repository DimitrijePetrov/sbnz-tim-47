package tests;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import org.drools.core.ClockType;
import org.drools.core.time.SessionPseudoClock;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class WarningsCEPTest {
    protected final String ksessionName = "warningCEPSession";

    @Test
    public void testWindStormWarning() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSessionConfiguration kieConfiguration = kieServices.newKieSessionConfiguration();
        kieConfiguration.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
        KieSession kieSession = kieContainer.newKieSession(ksessionName, kieConfiguration);

        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        SessionPseudoClock clock = kieSession.getSessionClock();
        for (int i = 1; i <= 4; i++) {
            MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(threeHoursAgo.plusHours(i), 24.0, 50, 11.0, 50, 31.1);
            kieSession.insert(m);
            System.out.println("Objects inserted: " + i);

            int firedRules = kieSession.fireAllRules();
            System.out.println("Rules fired: " + firedRules);
            clock.advanceTime(1, TimeUnit.HOURS);
        }
    }

    @Test
    public void testFloodWarning() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSessionConfiguration kieConfiguration = kieServices.newKieSessionConfiguration();
        kieConfiguration.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
        KieSession kieSession = kieContainer.newKieSession(ksessionName, kieConfiguration);

        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        SessionPseudoClock clock = kieSession.getSessionClock();
        for (int i = 1; i <= 4; i++) {
            MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(threeHoursAgo.plusHours(i), 24.0, 50, 11.1, 50, 5.1);
            kieSession.insert(m);
            System.out.println("Objects inserted: " + i);

            int firedRules = kieSession.fireAllRules();
            System.out.println("Rules fired: " + firedRules);
            clock.advanceTime(1, TimeUnit.HOURS);
        }
    }

    @Test
    public void testHeatWaveWarning() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSessionConfiguration kieConfiguration = kieServices.newKieSessionConfiguration();
        kieConfiguration.setOption(ClockTypeOption.get(ClockType.PSEUDO_CLOCK.getId()));
        KieSession kieSession = kieContainer.newKieSession(ksessionName, kieConfiguration);

        LocalDateTime threeHoursAgo = LocalDateTime.now().minusHours(3);
        SessionPseudoClock clock = kieSession.getSessionClock();
        for (int i = 1; i <= 4; i++) {
            MeasuredHourlyWeatherData m = new MeasuredHourlyWeatherData(threeHoursAgo.plusHours(i), 38.0, 50, 0.0, 50, 2.1);
            kieSession.insert(m);
            System.out.println("Objects inserted: " + i);

            int firedRules = kieSession.fireAllRules();
            System.out.println("Rules fired: " + firedRules);
            clock.advanceTime(1, TimeUnit.HOURS);
        }
    }
}
