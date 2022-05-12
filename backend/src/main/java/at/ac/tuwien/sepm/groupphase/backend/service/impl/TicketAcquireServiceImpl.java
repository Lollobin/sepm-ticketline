package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationFacade;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketAcquireService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.PurchaseValidator;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TicketAcquireServiceImpl implements TicketAcquireService {

    private final PurchaseValidator purchaseValidator;
    private final TicketRepository ticketRepository;
    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public TicketAcquireServiceImpl(PurchaseValidator purchaseValidator,
        TicketRepository ticketRepository, AuthenticationFacade authenticationFacade,
        UserRepository userRepository) {
        this.purchaseValidator = purchaseValidator;
        this.ticketRepository = ticketRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    @Override
    public FullTicketWithStatusDto acquireTickets(TicketStatusDto ticketsToAcquire) {
        boolean purchaseMode = ticketsToAcquire.getReserved().isEmpty();
        this.purchaseValidator.validateTicketInformation(ticketsToAcquire);
        List<Ticket> ticketList = ticketRepository.findAllById(
            purchaseMode ? ticketsToAcquire.getPurchased() : ticketsToAcquire.getReserved());

        List<Ticket> unavailableTickets = getUnavailableTickets(ticketList);
        if (!unavailableTickets.isEmpty()) {
            //TODO: CHANGE TO CONFLICT ERROR and add ticket info
            throw new ValidationException("TICKETS NOT AVAILABLE");
        }
        List<Ticket> updatedTickets = updateTicketStatus(
            purchaseMode, ticketList);
        FullTicketWithStatusDto fullTicketWithStatusDto = new FullTicketWithStatusDto();
        List<TicketDto> fullTickets = updatedTickets.stream().map(this::ticketToTicketDto).toList();
        if (purchaseMode) {
            fullTicketWithStatusDto.setPurchased(fullTickets);
        } else {
            fullTicketWithStatusDto.setReserved(fullTickets);
        }
        return fullTicketWithStatusDto;
    }

    private TicketDto ticketToTicketDto(Ticket ticket) {
            TicketDto ticketDto = new TicketDto();
            ticketDto.setTicketId(ticket.getTicketId());
            ticketDto.setSector(ticket.getSeat().getSector().getSectorId());
            ticketDto.setSeatNumber(ticket.getSeat().getSeatNumber());
            ticketDto.setRowNumber(ticket.getSeat().getRowNumber());
            return ticketDto;
    }

    private List<Ticket> updateTicketStatus(boolean purchaseMode, List<Ticket> ticketList) {
        ApplicationUser user = this.userRepository.findUserByEmail(
            authenticationFacade.getAuthentication().getPrincipal().toString());
        for (Ticket ticket : ticketList) {
            ticket.setReservedBy(user);
            if (purchaseMode) {
                ticket.setPurchasedBy(user);
            }
        }
        List<Ticket> updatedTickets = ticketRepository.saveAll(ticketList);
        return updatedTickets;
    }

    private List<Ticket> getUnavailableTickets(List<Ticket> ticketList) {
        List<Ticket> unavailableTickets = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (!(ticket.getPurchasedBy() == null || ticket.getReservedBy() == null)) {
                unavailableTickets.add(ticket);
            }
        }
        return unavailableTickets;
    }
}
