package tests;

import com.weatherforecast.model.MeasuredHourlyWeatherData;
import com.weatherforecast.service.Application;
import org.drools.template.DataProvider;
import org.drools.template.DataProviderCompiler;
import org.drools.template.objects.ArrayDataProvider;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.springframework.boot.system.ApplicationHome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

public class TemperatureTemplateTest {
    protected final String ksessionName = "temperatureTemplateSession";

    @Test
    public void testGetAverageTemperatureForThisHourInThePastThreeDays() throws FileNotFoundException {
        ApplicationHome home = new ApplicationHome(TemperatureTemplateTest.class);
        String path = home.getDir().getParent().concat("\\kjar\\src\\main\\resources\\rules\\template\\temperatureTemplate.drt");
        InputStream template = new FileInputStream(path);

        String[][] hours = new String[24][1];
        for (int i = 0; i <= 23; i++) {
            String hourString = String.valueOf(i + 1);
            hours[i] = new String[] {hourString};
        }
        DataProvider dataProvider = new ArrayDataProvider(hours);

        DataProviderCompiler converter = new DataProviderCompiler();
        String drl = converter.compile(dataProvider, template);

//        System.out.println(drl);

        KieSession kSession = createKieSessionFromDRL(drl);

        for (int i = 1; i <= 24; i++) {
            LocalDateTime iHoursFromNow = LocalDateTime.now().plusHours(i);
            MeasuredHourlyWeatherData m1 = new MeasuredHourlyWeatherData(iHoursFromNow.minusDays(1), 24.0+i, 50, 11.0, 50, 1.1);
            MeasuredHourlyWeatherData m2 = new MeasuredHourlyWeatherData(iHoursFromNow.minusDays(2), 22.0+i, 50, 0.0, 50, 1.1);
            MeasuredHourlyWeatherData m3 = new MeasuredHourlyWeatherData(iHoursFromNow.minusDays(3), 20.0+i, 50, 11.2, 50, 1.1);

            kSession.insert(m1);
            kSession.insert(m2);
            kSession.insert(m3);
            kSession.fireAllRules();
        }

//        doTest(ksession);
    }

    private KieSession createKieSessionFromDRL(String drl){
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
