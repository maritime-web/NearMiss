package dk.dma.nearmiss.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dma.nearmiss.rest.generated.api.NearMissApi;
import dk.dma.nearmiss.rest.generated.model.NearMissResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Optional;

@SuppressWarnings("DefaultAnnotationParam")
@Controller
public class NearMissApiController implements NearMissApi {
    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Override
    public ResponseEntity<NearMissResponse> events(OffsetDateTime from, OffsetDateTime to) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @org.springframework.beans.factory.annotation.Autowired
    public NearMissApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
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
