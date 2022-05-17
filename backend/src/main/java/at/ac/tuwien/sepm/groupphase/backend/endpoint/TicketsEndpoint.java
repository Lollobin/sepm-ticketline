package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.TicketsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketAcquireService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketsEndpoint implements TicketsApi {

    private final TicketAcquireService ticketAcquireService;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public TicketsEndpoint(TicketAcquireService ticketAcquireService) {
        this.ticketAcquireService = ticketAcquireService;
    }

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<FullTicketWithStatusDto> ticketsPost(TicketStatusDto ticketStatusDto) {
        LOGGER.info("POST tickets/ with body {}", ticketStatusDto);
        FullTicketWithStatusDto tickets = this.ticketAcquireService.acquireTickets(ticketStatusDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(tickets);
    }
}
