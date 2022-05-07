package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.SeatingPlanLayoutsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingPlanLayoutService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
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
    public ResponseEntity<Resource> seatingPlanLayoutsIdGet(Long id) {
        LOGGER.info("GET /seatingPlanLayouts/{}", id);
        return ResponseEntity.ok(seatingPlanLayoutService.findOne(id));
    }
}
