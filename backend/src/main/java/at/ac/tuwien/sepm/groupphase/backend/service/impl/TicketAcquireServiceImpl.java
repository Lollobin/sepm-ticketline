package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketAcquireService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.PurchaseValidator;
import org.springframework.stereotype.Service;

@Service
public class TicketAcquireServiceImpl implements TicketAcquireService {

    private final PurchaseValidator purchaseValidator;

    public TicketAcquireServiceImpl(PurchaseValidator purchaseValidator) {
        this.purchaseValidator = purchaseValidator;
    }

    @Override
    public FullTicketWithStatusDto acquireTickets(TicketStatusDto ticketsToAcquire) {
        this.purchaseValidator.validateTicketInformation(ticketsToAcquire);
        FullTicketWithStatusDto fullTicketWithStatusDto = new FullTicketWithStatusDto();
        return fullTicketWithStatusDto;
    }
}
