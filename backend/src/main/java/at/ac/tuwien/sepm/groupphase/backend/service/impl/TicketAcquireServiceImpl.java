package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
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
    private final AuthenticationUtil authenticationFacade;
    private final UserRepository userRepository;
    private final OrderService orderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public TicketAcquireServiceImpl(PurchaseValidator purchaseValidator,
        TicketRepository ticketRepository, AuthenticationUtil authenticationFacade,
        UserRepository userRepository, OrderService orderService) {
        this.purchaseValidator = purchaseValidator;
        this.ticketRepository = ticketRepository;
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @Override
    public FullTicketWithStatusDto acquireTickets(TicketStatusDto ticketsToAcquire) {
        boolean purchaseMode = ticketsToAcquire.getReserved().isEmpty();
        this.purchaseValidator.validateTicketInformation(ticketsToAcquire);
        List<Ticket> ticketList = ticketRepository.findAllById(
            purchaseMode ? ticketsToAcquire.getPurchased() : ticketsToAcquire.getReserved());
        ApplicationUser user = this.userRepository.findUserByEmail(
            authenticationFacade.getAuthentication().getPrincipal().toString());
        List<Ticket> unavailableTickets = getUnavailableTickets(ticketList, user);
        if (!unavailableTickets.isEmpty()) {
            throw new ValidationException(unavailableTickets.size() + " ticket(s) not available");
        }
        List<Ticket> updatedTickets = updateTicketStatus(purchaseMode, ticketList, user);
        this.orderService.generateTransaction(ticketList, user,
            purchaseMode ? BookingType.PURCHASE : BookingType.RESERVATION);

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

    private List<Ticket> updateTicketStatus(boolean purchaseMode, List<Ticket> ticketList,
        ApplicationUser user) {

        for (Ticket ticket : ticketList) {
            ticket.setReservedBy(user);
            if (purchaseMode) {
                ticket.setPurchasedBy(user);
            }
        }
        List<Ticket> updatedTickets = ticketRepository.saveAllAndFlush(ticketList);
        return updatedTickets;
    }

    private List<Ticket> getUnavailableTickets(List<Ticket> ticketList, ApplicationUser user) {
        List<Ticket> unavailableTickets = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (ticket.getPurchasedBy() != null || (ticket.getReservedBy() != null
                && ticket.getReservedBy().getUserId() != user.getUserId())) {
                unavailableTickets.add(ticket);
            }
        }
        return unavailableTickets;
    }
}
