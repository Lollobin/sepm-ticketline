package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedInRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
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
    private final Faker faker = new Faker();

    public TransactionBookedInGenerator(
        TransactionRepository transactionRepository,
        UserRepository userRepository,
        BookedInRepository bookedInRepository,
        TicketRepository ticketRepository,
        ShowRepository showRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bookedInRepository = bookedInRepository;
        this.ticketRepository = ticketRepository;
        this.showRepository = showRepository;
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

                for (int j = 0; j < numberOfTicketsToBuy; j++) {
                    Ticket randomTicket = showTickets.get(j);
                    randomTicket.setPurchasedBy(user);
                    ticketRepository.save(randomTicket);

                    // TODO: also generate cancellations, reservations, dereservations
                    BookedIn bookedIn = generateBookedIn(transaction, randomTicket,
                        BookingType.PURCHASE);
                    bookedInRepository.save(bookedIn);
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
        // TODO: set price at booking time correctly
        bookedIn.setPriceAtBookingTime(BigDecimal.valueOf(1));
        return bookedIn;
    }
}
