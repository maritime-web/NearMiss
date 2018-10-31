package dk.dma.nearmiss.engine;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("near.miss")
public class NearMissEngineConfiguration {
    private Integer interval;

    public Integer getInterval() {
        return interval == null ? 0 : interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }
}
