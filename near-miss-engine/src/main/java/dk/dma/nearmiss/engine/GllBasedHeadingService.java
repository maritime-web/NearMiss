package dk.dma.nearmiss.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@Primary
public class GllBasedHeadingService implements HeadingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CourseOverGroundService courseOverGroundService;
    private int heading;

    public GllBasedHeadingService(CourseOverGroundService courseOverGroundService) {
        this.courseOverGroundService = courseOverGroundService;
    }

    @Override
    public void update(String message) {
        courseOverGroundService.update(message);

        int courseOverGround = courseOverGroundService.courseOverGround();
        if (courseOverGround >= 0) {
            heading = (int) (courseOverGround + (Math.random() * 10 - 5)) % 360;
            if (heading < 0)
                heading += 360;
        } else {
            heading = -1;
        }
    }

    @Override
    public int heading() {
        return heading;
    }

    @Override
    public LocalTime timeOfLastUpdate() {
        return courseOverGroundService.timeOfLastUpdate();
    }

}
