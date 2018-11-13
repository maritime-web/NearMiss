package dk.dma.nearmiss.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dma.nearmiss.db.repository.VesselStateRepository;
import dk.dma.nearmiss.rest.generated.api.NearMissApi;
import dk.dma.nearmiss.rest.generated.model.NearMissResponse;
import dk.dma.nearmiss.rest.generated.model.VesselState;
import dk.dma.nearmiss.web.VesselStateConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("DefaultAnnotationParam")
@Controller
public class NearMissApiController implements NearMissApi {
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private final VesselStateRepository repository;

    @org.springframework.beans.factory.annotation.Autowired
    public NearMissApiController(ObjectMapper objectMapper, HttpServletRequest request, VesselStateRepository repository) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.repository = repository;
    }

    @Override
    public ResponseEntity<NearMissResponse> events(OffsetDateTime from, OffsetDateTime to, Boolean onlyNearMissStates) {
        boolean onlyNearMiss = onlyNearMissStates != null && !Boolean.FALSE.equals(onlyNearMissStates);
        List<dk.dma.nearmiss.db.entity.VesselState> entities = repository.list(from, to, onlyNearMiss);

        if (entities.isEmpty()) return new ResponseEntity<>(HttpStatus.OK);

        List<VesselState> models = entities.stream().map(e -> new VesselStateConverter(e).convert()).collect(Collectors.toList());

        NearMissResponse response = new NearMissResponse();
        response.setVesselStates(models);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public Optional<ObjectMapper> getObjectMapper() {
        return Optional.ofNullable(objectMapper);
    }

    @Override
    public Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(request);
    }

}
