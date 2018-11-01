package dk.dma.nearmiss.engine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NearMissConfigurationTests {
    @Autowired
    private NearMissEngineConfiguration conf;

    @Test
    public void interval() {
        Integer expectedValueFromConf = 5;
        Integer actualValueFromConf = conf.getInterval();
        assertNotNull("Expected interval configuration to have value", actualValueFromConf);
        assertEquals("Expected right value from configuration", expectedValueFromConf, actualValueFromConf);
    }

    @Test
    public void stringDate() {
        String stringDate = conf.getStringDate();
        assertNotNull("Expected date configuration to have value", stringDate);
    }

    @Test
    public void date() {
        LocalDate date = conf.getDate();
        assertNotNull("Expected date to have value", date);
    }

}
