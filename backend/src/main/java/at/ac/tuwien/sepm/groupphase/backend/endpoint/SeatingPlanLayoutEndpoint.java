package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanLayoutDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.SeatingPlanLayoutsApi;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingPlanLayoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeatingPlanLayoutEndpoint implements SeatingPlanLayoutsApi {
    private final SeatingPlanLayoutService seatingPlanLayoutService;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public SeatingPlanLayoutEndpoint(SeatingPlanLayoutService seatingPlanLayoutService) {
        this.seatingPlanLayoutService = seatingPlanLayoutService;
    }

    @Override
    public ResponseEntity<SeatingPlanLayoutDto> seatingPlanLayoutsIdGet(Long id) {
        LOGGER.info("GET /seatingPlanLayouts/{}", id);
        ObjectMapper mapper = new ObjectMapper();
        try {
            return ResponseEntity.ok(mapper.readValue(seatingPlanLayoutService.findOne(id).getFile(), SeatingPlanLayoutDto.class));
        } catch (IOException e) {
            throw new NotFoundException("Error while reading the layout with ID " + id);
        }
    }
}
