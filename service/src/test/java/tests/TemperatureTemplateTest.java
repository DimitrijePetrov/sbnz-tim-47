package tests;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.boot.system.ApplicationHome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class TemperatureTemplateTest {
    @Test
    public void testPredictTemperatureForNext24HoursContinually() throws FileNotFoundException {
        for (int i = 0; i < 10; i++) {
            KieSession kieSession = createKieSession(LocalDateTime.now().plusHours(i));
            int firedRules = kieSession.fireAllRules();
            MatcherAssert.assertThat(firedRules, equalTo(24));

            for (int j = 1; j <= 24; j++) {
                LocalDateTime iHoursFromNow = LocalDateTime.now().plusHours(i+j);

                MeasuredHourlyWeatherData m1 = new MeasuredHourlyWeatherData(iHoursFromNow.minusDays(1), 24.0, 50, 11.0, 50, 1.1);
                MeasuredHourlyWeatherData m2 = new MeasuredHourlyWeatherData(iHoursFromNow.minusDays(2), 22.0, 50, 0.0, 50, 1.1);
                MeasuredHourlyWeatherData m3 = new MeasuredHourlyWeatherData(iHoursFromNow.minusDays(3), 20.0, 50, 11.2, 50, 1.1);

                kieSession.insert(m1);
                kieSession.insert(m2);
                kieSession.insert(m3);

                firedRules = kieSession.fireAllRules();
                MatcherAssert.assertThat(firedRules, equalTo(3));
            }
        }
    }

    private KieSession createKieSession(LocalDateTime startingDateTime) throws FileNotFoundException {
        ApplicationHome home = new ApplicationHome(TemperatureTemplateTest.class);
        String path = home.getDir().getParent().concat("\\kjar\\src\\main\\resources\\rules\\template\\temperatureTemplate.drt");
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
}
