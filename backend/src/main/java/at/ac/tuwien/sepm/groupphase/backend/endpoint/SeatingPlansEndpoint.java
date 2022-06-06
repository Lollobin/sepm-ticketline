package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.SeatingPlansApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectorMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingPlanService;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class SeatingPlansEndpoint implements SeatingPlansApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final SectorService sectorService;
    private final SectorMapper sectorMapper;
    private final SeatingPlanService seatingPlanService;

    public SeatingPlansEndpoint(SectorService sectorService, SectorMapper sectorMapper, SeatingPlanService seatingPlanService) {
        this.sectorService = sectorService;
        this.sectorMapper = sectorMapper;
        this.seatingPlanService = seatingPlanService;
    }

    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<List<SectorDto>> seatingPlansIdSectorsGet(Long id) {

        LOGGER.info(String.format("GET /seatingPlans/%s/sectors", id));

        return new ResponseEntity<>(
            sectorService.findAllBySeatingPlan(id).stream().map(sectorMapper::sectorToSectorDto).toList(), HttpStatus.OK);

    }

    @Secured("ROLE_ADMIN")
    @Override
    public ResponseEntity<Void> seatingPlansPost(SeatingPlanWithoutIdDto seatingPlanWithoutIdDto) {
        LOGGER.info("POST /seatingPlans with body  {}", seatingPlanWithoutIdDto);
        Long seatingPlanId = seatingPlanService.createSeatingPlan(seatingPlanWithoutIdDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(seatingPlanId).toUri();

        return ResponseEntity.created(location).build();
    }
}
