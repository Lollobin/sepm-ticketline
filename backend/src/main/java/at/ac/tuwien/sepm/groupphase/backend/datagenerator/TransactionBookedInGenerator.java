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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
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

    public TransactionBookedInGenerator(
        TransactionRepository transactionRepository,
        UserRepository userRepository,
        BookedInRepository bookedInRepository,
        TicketRepository ticketRepository,
        ShowRepository showRepository,
        SectorPriceRepository sectorPriceRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bookedInRepository = bookedInRepository;
        this.ticketRepository = ticketRepository;
        this.showRepository = showRepository;
        this.sectorPriceRepository = sectorPriceRepository;
    }

    public void generateData() {
        if (!transactionRepository.findAll().isEmpty()) {
            LOGGER.debug("transactions already generated");
            return;
        }

        LOGGER.debug("generating transactions and bookedIns for each user");

        List<ApplicationUser> users = userRepository.findAll();
        for (ApplicationUser user : users) {
            int numberOfPurchases = faker.number().numberBetween(1, 5);

            for (int i = 0; i < numberOfPurchases; i++) {
                long numberOfShows = showRepository.findAll().size();
                Show randomShow = showRepository.getByShowId(
                    faker.number().numberBetween(1L, numberOfShows));

                Transaction transaction = generateTransaction(user, randomShow.getDate());
                transactionRepository.save(transaction);

                List<Ticket> showTickets = ticketRepository.getByShowShowIdAndPurchasedByIsNullAndReservedByIsNull(
                    randomShow.getShowId());

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
                    Ticket randomTicket = showTickets.get(j);

                    if (bookingType == BookingType.PURCHASE) {
                        randomTicket.setPurchasedBy(user);
                    } else {
                        randomTicket.setReservedBy(user);
                    }

                    ticketRepository.save(randomTicket);

                    BookedIn bookedIn = generateBookedIn(transaction, randomTicket,
                        bookingType);
                    bookedInRepository.save(bookedIn);
                }

                transaction = transactionRepository.getByTransactionId(
                    (transaction.getTransactionId()));

                if (transaction.getBookedIns() == null) {
                    LOGGER.warn("created transaction without any bookedIns");
                    transactionRepository.delete(transaction);
                } else {
                    // generate cancellations and dereservations for some tickets
                    random = faker.number().randomDouble(3, 0, 1);
                    if (random < 0.2) {
                        Transaction transaction2 = generateTransaction(user, randomShow.getDate());
                        transaction2.setDate(transaction.getDate().plusDays(2));
                        transactionRepository.save(transaction2);

                        if (bookingType == BookingType.PURCHASE) {
                            bookingType = BookingType.CANCELLATION;
                        } else {
                            bookingType = BookingType.DERESERVATION;
                        }

                        Set<BookedIn> bookedIns = transaction.getBookedIns();
                        for (BookedIn bookedIn : bookedIns) {
                            bookedIn.getTicket().setReservedBy(null);
                            bookedIn.getTicket().setPurchasedBy(null);

                            BookedIn bookedIn2 = generateBookedIn(transaction2, bookedIn.getTicket(),
                                bookingType);
                            bookedInRepository.save(bookedIn2);
                        }
                    }
                }
            }
        }
    }

    private Transaction generateTransaction(ApplicationUser user, OffsetDateTime beforeDate) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        OffsetDateTime transactionDate = beforeDate
            .minusDays(faker.number().numberBetween(0, 500))
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
        bookedIn.setPriceAtBookingTime(
            sectorPriceRepository.findOneByShowIdBySectorId(ticket.getShow().getShowId(),
                ticket.getSeat().getSector().getSectorId()).getPrice());
        return bookedIn;
    }
}
