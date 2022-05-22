package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.BookedInKey;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedInRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            MethodHandles.lookup().lookupClass());
    private final TransactionRepository transactionRepository;
    private final AuthenticationUtil authenticationFacade;
    private final SectorPriceRepository sectorPriceRepository;
    private final BookedInRepository bookedInRepository;

    public OrderServiceImpl(
            TransactionRepository transactionRepository, AuthenticationUtil authenticationFacade,
            SectorPriceRepository sectorPriceRepository, BookedInRepository bookedInRepository) {
        this.transactionRepository = transactionRepository;
        this.authenticationFacade = authenticationFacade;
        this.sectorPriceRepository = sectorPriceRepository;
        this.bookedInRepository = bookedInRepository;
    }

    @Override
    public List<Transaction> findAllByCurrentUser() {
        String email = authenticationFacade.getEmail();
        LOGGER.debug("Looking for orders by '{}'", email);

        return transactionRepository.findAllByUserEmailOrderByDateDesc(email);
    }

    @Override
    public void generateTransaction(List<Ticket> tickets, ApplicationUser user,
            BookingType bookingType) {
        LOGGER.debug("Generate Transaction for tickets {} with user {} of type {}", tickets, user,
                bookingType);
        if (tickets.isEmpty()) {
            return;
        }
        Transaction transaction = new Transaction();
        transaction.setDate(OffsetDateTime.now().toLocalDate());
        transaction.setUser(user);
        // TODO: ADD GENERATION OF BILL
        transaction.setBillPath(null);
        this.transactionRepository.save(transaction);
        Set<BookedIn> bookings = tickets.stream().map(ticket -> {
            BookedInKey key = new BookedInKey();
            key.setTransactionId(transaction.getTransactionId());
            key.setTicketId(ticket.getTicketId());
            BookedIn bookedIn = new BookedIn();
            bookedIn.setId(key);
            bookedIn.setTransaction(transaction);
            bookedIn.setBookingType(bookingType);
            bookedIn.setTicket(ticket);
            SectorPrice sectorPrice = this.sectorPriceRepository.findOneByShowIdBySectorId(
                    ticket.getShow().getShowId(),
                    ticket.getSeat().getSector().getSectorId());
            bookedIn.setPriceAtBookingTime(sectorPrice.getPrice());

            return bookedIn;
        }).collect(Collectors.toSet());
        this.bookedInRepository.saveAll(bookings);
    }
}
