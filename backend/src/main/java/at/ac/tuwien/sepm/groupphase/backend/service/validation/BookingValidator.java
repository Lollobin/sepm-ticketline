package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class BookingValidator {
    public void validateTicketsToAcquire(TicketStatusDto ticketStatusDto) {
        if (!ticketStatusDto.getPurchased().isEmpty()
            && !ticketStatusDto.getReserved().isEmpty()) {
            throw new ValidationException(
                "Currently only purchasing or reserving is allowed, not both");
        }
        if (ticketStatusDto.getPurchased().isEmpty() && ticketStatusDto.getReserved().isEmpty()) {
            throw new ValidationException("At least one ticket has to be bought or reserved.");
        }
    }

    public void validateTicketsToCancel(TicketStatusDto ticketStatusDto) {
        if (!ticketStatusDto.getPurchased().isEmpty()
            && !ticketStatusDto.getReserved().isEmpty()) {
            throw new ValidationException(
                "Currently only cancelling of purchases or reservations is allowed, not both");
        }
        if (ticketStatusDto.getPurchased().isEmpty() && ticketStatusDto.getReserved().isEmpty()) {
            throw new ValidationException("At least one ticket has to be cancelled");
        }
    }
}
