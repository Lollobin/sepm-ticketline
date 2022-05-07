package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.TicketsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketAcquireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketsEndpoint implements TicketsApi {

    private final TicketAcquireService ticketAcquireService;

    public TicketsEndpoint(TicketAcquireService ticketAcquireService) {
        this.ticketAcquireService = ticketAcquireService;
    }

    @Override
    public ResponseEntity<FullTicketWithStatusDto> ticketsPost(TicketStatusDto ticketStatusDto) {
        FullTicketWithStatusDto tickets = this.ticketAcquireService.acquireTickets(ticketStatusDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(tickets);
    }
}
