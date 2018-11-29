package dk.dma.nearmiss.engine.nmeaBasedServices;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@Primary
public class GllBasedHeadingService implements HeadingService {

    @Override
    public int update(int heading, int courseOverGround, String message) {
        if (courseOverGround >= 0) {
            heading = (int) (courseOverGround + (Math.random() * 10 - 5)) % 360;
            if (heading < 0)
                heading += 360;
        } else {
            heading = -1;
        }
        return heading;
    }

}
