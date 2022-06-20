package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.TicketPrintsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketPrintService;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketPrintsEndpoint implements TicketPrintsApi {

    private final TicketPrintService ticketPrintService;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public TicketPrintsEndpoint(TicketPrintService ticketPrintService) {
        this.ticketPrintService = ticketPrintService;
    }

    @Override
    public ResponseEntity<Resource> ticketPrintsGet(List<Long> tickets) {
        LOGGER.info("GET /ticketPrints");
        try {
            return ResponseEntity.ok().body(ticketPrintService.getTicketPdf(tickets));
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
