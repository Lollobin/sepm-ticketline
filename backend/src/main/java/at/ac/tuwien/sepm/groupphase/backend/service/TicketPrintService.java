package at.ac.tuwien.sepm.groupphase.backend.service;

import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;

public interface TicketPrintService {

    /**
     * Creates a PDF for a given Ticket ID and returns it.
     *
     * @param tickets The IDs of the tickets
     * @return Resource for PDF
     */
    Resource getTicketPdf(List<Long> tickets) throws IOException;

}
