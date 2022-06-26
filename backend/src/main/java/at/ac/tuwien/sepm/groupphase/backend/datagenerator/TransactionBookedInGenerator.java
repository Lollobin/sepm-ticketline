package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedInRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class TransactionBookedInGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BookedInRepository bookedInRepository;
    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    private final SectorPriceRepository sectorPriceRepository;
    private final Faker faker = new Faker();

    public TransactionBookedInGenerator(TransactionRepository transactionRepository,
        UserRepository userRepository, BookedInRepository bookedInRepository,
        TicketRepository ticketRepository, ShowRepository showRepository,
        SectorPriceRepository sectorPriceRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bookedInRepository = bookedInRepository;
        this.ticketRepository = ticketRepository;
        this.showRepository = showRepository;
        this.sectorPriceRepository = sectorPriceRepository;
    }

    private Map<Long, List<Ticket>> generateMapAvailableShowTickets() {
        List<Ticket> allTickets = ticketRepository.findAll();
        HashMap<Long, List<Ticket>> ticketMap = new HashMap<>();
        for (Ticket ticket : allTickets) {
            Long showId = ticket.getShow().getShowId();
            if (ticket.getPurchasedBy() == null && ticket.getReservedBy() == null) {
                ticketMap.computeIfAbsent(showId, k -> new ArrayList<>());
                ticketMap.get(showId).add(ticket);
            }
        }
        return ticketMap;
    }

    public void generateData() {
        if (!transactionRepository.findAll().isEmpty()) {
            LOGGER.debug("transactions already generated");
            return;
        }

        LOGGER.debug("generating transactions and bookedIns for each user");
        List<Show> allShows = showRepository.findAll();

        List<ApplicationUser> users = userRepository.findAll();
        Map<Long, List<Ticket>> ticketMap = generateMapAvailableShowTickets();
        List<Transaction> transactions = new ArrayList<>();
        List<BookedIn> bookedIns = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();
        for (ApplicationUser user : users) {
            int numberOfPurchases = faker.number().numberBetween(1, 40);
            for (int i = 0; i < numberOfPurchases; i++) {
                Show randomShow = allShows.get(faker.number().numberBetween(1, allShows.size()));

                Transaction transaction = generateTransaction(user, randomShow.getDate());
                transactions.add(transaction);

                List<Ticket> showTickets = ticketMap.get(randomShow.getShowId());

                int totalNumberOfTickets = showTickets.size();
                int numberOfTicketsToBuy = faker.number().numberBetween(1, 5);
                if (numberOfTicketsToBuy > totalNumberOfTickets) {
                    numberOfTicketsToBuy = totalNumberOfTickets;
                }

                BookingType bookingType;
                double random = faker.number().randomDouble(3, 0, 1);
                if (random < 0.5) {
                    bookingType = BookingType.PURCHASE;
                } else {
                    bookingType = BookingType.RESERVATION;
                }

                for (int j = 0; j < numberOfTicketsToBuy; j++) {
                    int randomNumber = (int) Math.floor(Math.random() * showTickets.size());
                    Ticket randomTicket = showTickets.get(randomNumber);

                    if (bookingType == BookingType.PURCHASE) {
                        randomTicket.setPurchasedBy(user);
                    } else {
                        randomTicket.setReservedBy(user);
                    }
                    tickets.add(randomTicket);
                    showTickets.remove(randomNumber);
                    BookedIn bookedIn = generateBookedIn(transaction, randomTicket, bookingType);
                    bookedIns.add(bookedIn);
                }
            }
        }
        transactionRepository.saveAll(transactions);
        ticketRepository.saveAll(tickets);
        bookedInRepository.saveAll(bookedIns);
        LOGGER.debug("generating cancellations and dereservations for each user");

        List<Transaction> savedTransactions = transactionRepository.findAll();
        List<Transaction> transactionsToDelete = new ArrayList<>();
        List<BookedIn> bookedInsToDelete = new ArrayList<>();
        for (Transaction transaction : savedTransactions) {

            if (transaction.getBookedIns() == null || transaction.getBookedIns().isEmpty()) {
                transactionsToDelete.add(transaction);
                continue;
            }

            // generate cancellations and dereservations for some tickets
            double random = faker.number().randomDouble(3, 0, 1);
            if (random < 0.2) {
                Transaction transaction2 = generateTransaction(transaction.getUser(),
                    transaction.getDate());
                transaction2.setDate(transaction.getDate().plusDays(2));
                transactionRepository.save(transaction2);

                for (BookedIn bookedIn : transaction.getBookedIns()) {
                    bookedIn.getTicket().setReservedBy(null);
                    bookedIn.getTicket().setPurchasedBy(null);

                    bookedInsToDelete.add(generateBookedIn(transaction2,
                        bookedIn.getTicket(),
                        bookedIn.getBookingType().equals(BookingType.PURCHASE) ? BookingType.CANCELLATION
                            : BookingType.DERESERVATION));
                }

            }
        }
        bookedInRepository.saveAll(bookedInsToDelete);
        transactionRepository.deleteAll(transactionsToDelete);
    }

    private Transaction generateTransaction(ApplicationUser user, OffsetDateTime beforeDate) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        OffsetDateTime earlierDate =
            beforeDate.isBefore(OffsetDateTime.now()) ? beforeDate : OffsetDateTime.now();
        OffsetDateTime transactionDate = earlierDate.minusDays(faker.number().numberBetween(0, 500))
            .minusMinutes(faker.number().numberBetween(0, 1000));
        transaction.setDate(transactionDate);
        return transaction;
    }

    private BookedIn generateBookedIn(Transaction transaction, Ticket ticket,
        BookingType bookingType) {
        BookedIn bookedIn = new BookedIn();
        bookedIn.setTransaction(transaction);
        bookedIn.setTicket(ticket);
        bookedIn.setBookingType(bookingType);
        bookedIn.setPriceAtBookingTime(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100)));
        return bookedIn;
    }
}
