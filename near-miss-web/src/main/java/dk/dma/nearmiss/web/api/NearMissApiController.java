package dk.dma.nearmiss.web.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dma.nearmiss.rest.generated.api.NearMissApi;
import dk.dma.nearmiss.rest.generated.model.NearMissResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Optional;

@SuppressWarnings("DefaultAnnotationParam")
@Controller
public class NearMissApiController implements NearMissApi {
    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;


    @ApiOperation(value = "Get near miss events for own vessel in the specified time interval", nickname = "events", notes = "", response = NearMissResponse.class, tags = {"events",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = NearMissResponse.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Server Error"),
            @ApiResponse(code = 501, message = "Server Error")})
    @RequestMapping(value = "/near-miss",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<NearMissResponse> events(@NotNull @ApiParam(value = "Start time of the interval in UTC", required = true) @Valid @RequestParam(value = "from", required = true) OffsetDateTime from, @NotNull @ApiParam(value = "End time of the interval in UTC", required = true) @Valid @RequestParam(value = "to", required = true) OffsetDateTime to) {
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
