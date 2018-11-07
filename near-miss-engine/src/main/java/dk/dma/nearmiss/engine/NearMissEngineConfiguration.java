package dk.dma.nearmiss.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@ConfigurationProperties(value = "near.miss")
public class NearMissEngineConfiguration {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Value("${near.miss.interval:0}")
    private Integer interval;

    @Value("${near.miss.save.all.positions:false}")
    private boolean saveAllPositions;

    @Value("${near.miss.date:}")
    private String stringDate;
    private LocalDate date;

    @Value("${near.miss.own.ship.mmsi:0}")
    private Integer ownShipMmsi;

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

    public void setOwnShipMmsi(Integer ownShipMmsi) {
        this.ownShipMmsi = ownShipMmsi;
    }
}