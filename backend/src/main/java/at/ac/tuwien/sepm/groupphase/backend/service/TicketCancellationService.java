package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;

public interface TicketCancellationService {

    /**
     * Checks if the tickets are valid and cancels them.
     *
     * @param ticketsToCancel Ids of tickets to be cancelled
     * @return Ids of the cancelled tickets
     */
    TicketStatusDto cancelTickets(TicketStatusDto ticketsToCancel);
}
