package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;

public interface TicketAcquireService {

    /**
     * Checks if tickets are available and either purchases them or reserves them.
     *
     * @param ticketsToAcquire The ids of the tickets that will be either purchased or acquired
     * @return Acquired tickets
     */
    FullTicketWithStatusDto acquireTickets(TicketStatusDto ticketsToAcquire);
}
