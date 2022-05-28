package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.TicketCancellationsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketCancellationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;

@RestController
public class TicketCancellationsEndpoint implements TicketCancellationsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final TicketCancellationService ticketCancellationService;

    public TicketCancellationsEndpoint(TicketCancellationService ticketCancellationService) {
        this.ticketCancellationService = ticketCancellationService;
    }

    @Secured("ROLE_USER")
    @Override
    public ResponseEntity<TicketStatusDto> ticketCancellationsPost(TicketStatusDto ticketStatusDto) {
        LOGGER.info("POST /ticketCancellations with body {}", ticketStatusDto);
        TicketStatusDto tickets = this.ticketCancellationService.cancelTickets(ticketStatusDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(tickets);
    }
}
