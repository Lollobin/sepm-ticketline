package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import java.util.List;

public interface TicketInfoService {

    /**
     * Returns all future tickets of the current user.
     *
     * @return all future tickets of the current user
     */
    List<Ticket> findAllByCurrentUser();
}
