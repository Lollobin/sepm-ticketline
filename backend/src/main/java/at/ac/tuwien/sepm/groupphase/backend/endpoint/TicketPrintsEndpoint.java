package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.TicketPrintsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketPrintService;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
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
    public ResponseEntity<Resource> ticketPrintsIdGet(Long id) {
        try {
            return ResponseEntity.ok().body(ticketPrintService.getTicketPdf(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
