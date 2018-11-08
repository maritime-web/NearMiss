package dk.dma.nearmiss.engine.engineParts;

import dk.dma.nearmiss.engine.Vessel;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/** Project vessel's position forward in time */
@Component
public class PositionPredicter implements Function<Vessel, Vessel> {

    @Override
    public Vessel apply(Vessel vessel) {
        // TODO implement
        return vessel;
    }

}
