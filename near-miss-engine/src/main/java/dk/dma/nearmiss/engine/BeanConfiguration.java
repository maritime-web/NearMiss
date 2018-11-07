package dk.dma.nearmiss.engine;

import dk.dma.ais.tracker.Tracker;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfiguration {

    @Bean
    public Tracker tracker() {
        return new TargetTracker();
    }

}
