package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CancellationValidator {
    public void validateTicketInformation(TicketStatusDto ticketStatusDto) {
        if (!ticketStatusDto.getPurchased().isEmpty()
            && !ticketStatusDto.getReserved().isEmpty()) {
            throw new ValidationException(
                "Currently only cancelling of purchases or reservations is allowed, not both");
        }
        if (ticketStatusDto.getPurchased().isEmpty() && ticketStatusDto.getReserved().isEmpty()) {
            throw new ValidationException("At least one ticket has to be cancelled.");
        }
    }
}
