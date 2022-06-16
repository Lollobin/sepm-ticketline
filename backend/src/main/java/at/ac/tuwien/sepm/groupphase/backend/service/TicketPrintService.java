package at.ac.tuwien.sepm.groupphase.backend.service;

import java.io.IOException;
import org.springframework.core.io.Resource;

public interface TicketPrintService {

    /**
     * Creates a PDF for a given Ticket ID and returns it.
     *
     * @param ticketId The ID of the ticket
     * @return Resource for PDF
     */
    Resource getTicketPdf(Long ticketId) throws IOException;

}
