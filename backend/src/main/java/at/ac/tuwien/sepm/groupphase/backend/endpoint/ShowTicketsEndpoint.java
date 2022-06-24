package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowInformationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ShowTicketsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowTicketService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShowTicketsEndpoint implements ShowTicketsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ShowTicketService showTicketService;

    public ShowTicketsEndpoint(ShowTicketService showTicketsEndpoint) {
        this.showTicketService = showTicketsEndpoint;
    }

    @Override
    public ResponseEntity<ShowInformationDto> showTicketsIdGet(Long id) {
        LOGGER.info("GET shows/{}", id);
        return ResponseEntity.ok(this.showTicketService.findOne(id));
    }
}
