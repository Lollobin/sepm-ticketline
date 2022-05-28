package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketCancellationService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.CancellationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketCancellationServiceImpl implements TicketCancellationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final CancellationValidator cancellationValidator;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuthenticationUtil authenticationUtil;
    private final OrderService orderService;

    public TicketCancellationServiceImpl(CancellationValidator cancellationValidator, TicketRepository ticketRepository, UserRepository userRepository, AuthenticationUtil authenticationUtil,
                                         OrderService orderService) {
        this.cancellationValidator = cancellationValidator;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.authenticationUtil = authenticationUtil;
        this.orderService = orderService;
    }

    @Override
    public TicketStatusDto cancelTickets(TicketStatusDto ticketsToCancel) {
        LOGGER.debug("Started ticket cancellation with following tickets: {}", ticketsToCancel);

        boolean purchaseMode = ticketsToCancel.getReserved().isEmpty();
        this.cancellationValidator.validateTicketInformation(ticketsToCancel);

        List<Ticket> ticketList = ticketRepository.findAllById(
            purchaseMode ? ticketsToCancel.getPurchased() : ticketsToCancel.getReserved());
        ApplicationUser user = this.userRepository.findUserByEmail(
            authenticationUtil.getEmail());
        List<Ticket> unavailableTickets = getUnavailableTickets(ticketList, user);

        if (!unavailableTickets.isEmpty()) {
            throw new ValidationException(unavailableTickets.size() + " ticket(s) not available");
        }

        updateTicketStatus(purchaseMode, ticketList);

        this.orderService.generateTransaction(ticketList, user,
            purchaseMode ? BookingType.CANCELLATION : BookingType.DERESERVATION);

        return ticketsToCancel;


    }

    private void updateTicketStatus(boolean purchaseMode, List<Ticket> ticketList) {
        for (Ticket ticket : ticketList) {
            ticket.setReservedBy(null);
            if (purchaseMode) {
                ticket.setPurchasedBy(null);
            }
        }
        ticketRepository.saveAllAndFlush(ticketList);
    }

    private List<Ticket> getUnavailableTickets(List<Ticket> ticketList, ApplicationUser user) {
        List<Ticket> unavailableTickets = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (ticket.getPurchasedBy() != null) {
                unavailableTickets.add(ticket);
            } else if (ticket.getReservedBy() != null) {
                if (ticket.getReservedBy().getUserId() != user.getUserId()) {
                    unavailableTickets.add(ticket);
                }
            }
        }
        return unavailableTickets;
    }

}
