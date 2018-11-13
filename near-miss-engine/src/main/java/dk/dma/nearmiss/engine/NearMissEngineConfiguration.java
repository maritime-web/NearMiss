package dk.dma.nearmiss.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@ConfigurationProperties(value = "near.miss")
public class NearMissEngineConfiguration {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${near.miss.interval:0}")
    private Integer interval;

    @Value("${near.miss.save.all.positions:false}")
    private boolean saveAllPositions;

    @Value("${near.miss.date:}")
    private String stringDate;
    private LocalDate date;

    @Value("${near.miss.own.ship.mmsi:219945000}")
    private Integer ownShipMmsi;

    @Value("${near.miss.own.ship.name:OWNSHIP}")
    private String ownShipName;

    @Value("${near.miss.own.ship.loa:999}")
    private Integer ownShipLoa;

    @Value("${near.miss.own.ship.beam:99}")
    private Integer ownShipBeam;

    @Value("${near.miss.screener.target.speed:0}")
    private Integer screenerMinSpeed;

    @Value("${near.miss.screener.target.recentupdate:10}")
    private Integer screenerMaxMinutesSinceLastUpdate;

    public Integer getInterval() {
        return interval == null ? 0 : interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }


    public LocalDate getDate() {
        if (date != null) return date;
        if (stringDate == null || stringDate.isEmpty()) {
            date = LocalDate.now();
        } else {
            date = LocalDate.parse(stringDate, formatter);
        }
        return date;
    }

    String getStringDate() {
        return stringDate;
    }

    public void setDate(String date) {
        this.stringDate = date;
    }

    public boolean isSaveAllPositions() {
        return saveAllPositions;
    }

    public void setSaveAllPositions(boolean saveAllPositions) {
        this.saveAllPositions = saveAllPositions;
    }

    public Integer getOwnShipMmsi() {
        return ownShipMmsi;
    }

    public String getOwnShipName() {
        return ownShipName;
    }

    public Integer getOwnShipLoa() {
        return ownShipLoa;
    }

    public Integer getOwnShipBeam() {
        return ownShipBeam;
    }

    public Integer getScreenerMinSpeed() {
        return screenerMinSpeed;
    }

    public Integer getScreenerMaxMinutesSinceLastUpdate() {
        return screenerMaxMinutesSinceLastUpdate;
    }

    @PostConstruct
    public void logConfiguration() {
        logger.info(toString());
    }

    @Override
    public String toString() {
        return "NearMissEngineConfiguration{" +
                "interval=" + interval +
                ", saveAllPositions=" + saveAllPositions +
                ", stringDate='" + stringDate + '\'' +
                ", date=" + date +
                ", ownShipMmsi=" + ownShipMmsi +
                ", ownShipName='" + ownShipName + '\'' +
                ", ownShipLoa=" + ownShipLoa +
                ", ownShipBeam=" + ownShipBeam +
                ", screenerMinSpeed=" + screenerMinSpeed +
                ", screenerMaxMinutesSinceLastUpdate=" + screenerMaxMinutesSinceLastUpdate +
                '}';
    }
}
