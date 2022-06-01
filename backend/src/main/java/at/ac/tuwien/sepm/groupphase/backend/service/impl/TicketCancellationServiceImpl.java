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
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TicketCancellationServiceImpl implements TicketCancellationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final CancellationValidator cancellationValidator;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuthenticationUtil authenticationUtil;
    private final OrderService orderService;

    public TicketCancellationServiceImpl(CancellationValidator cancellationValidator,
        TicketRepository ticketRepository, UserRepository userRepository,
        AuthenticationUtil authenticationUtil,
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

        this.cancellationValidator.validateTicketInformation(ticketsToCancel);

        BookingType bookingType = ticketsToCancel.getReserved().isEmpty() ? BookingType.CANCELLATION
            : BookingType.DERESERVATION;

        List<Ticket> ticketList = ticketRepository.findAllById(
            bookingType == BookingType.CANCELLATION ? ticketsToCancel.getPurchased()
                : ticketsToCancel.getReserved());
        ApplicationUser user = this.userRepository.findUserByEmail(
            authenticationUtil.getEmail());
        List<Ticket> unavailableTickets = getUnavailableTickets(ticketList, user, bookingType);

        if (!unavailableTickets.isEmpty()) {
            throw new ValidationException(unavailableTickets.size() + " ticket(s) not available");
        }

        for (Ticket ticket : ticketList) {
            ticket.setReservedBy(null);
            ticket.setPurchasedBy(null);
        }
        ticketRepository.saveAllAndFlush(ticketList);

        this.orderService.generateTransaction(ticketList, user, bookingType);

        return ticketsToCancel;
    }

    private List<Ticket> getUnavailableTickets(List<Ticket> ticketList, ApplicationUser user,
        BookingType bookingType) {
        List<Ticket> unavailableTickets = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            if (bookingType == BookingType.CANCELLATION) {
                if (ticket.getPurchasedBy().getUserId() != user.getUserId()) {
                    unavailableTickets.add(ticket);
                }
            }
            if (bookingType == BookingType.DERESERVATION) {
                if (ticket.getReservedBy().getUserId() != user.getUserId()) {
                    unavailableTickets.add(ticket);
                }
            }
        }
        return unavailableTickets;
    }
}
