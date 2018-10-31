package dk.dma.nearmiss.engine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
        assertNotNull("Expected interval congiguration to have value", actualValueFromConf);
        assertEquals("Expected right value from configuration", expectedValueFromConf, actualValueFromConf);
    }
}
