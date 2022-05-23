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
public class TransactionDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BookedInRepository bookedInRepository;
    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;
    private final Faker faker = new Faker();

    public TransactionDataGenerator(
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

    public void generateTransactions() {
        if (!transactionRepository.findAll().isEmpty()) {
            LOGGER.debug("transactions already generated");
            return;
        }

        LOGGER.debug("generating transactions and bookedIns for each user");

        List<ApplicationUser> users = userRepository.findAll();
        for (ApplicationUser user : users) {
            int numberOfPurchases = faker.number().numberBetween(1, 5);

            for (int i = 0; i < numberOfPurchases; i++) {
                Transaction transaction = new Transaction();
                transaction.setUser(user);

                long numberOfShows = showRepository.findAll().size();
                Show randomShow = showRepository.getByShowId(
                    faker.number().numberBetween(1L, numberOfShows));

                OffsetDateTime transactionDate = randomShow.getDate()
                    .minusDays(faker.number().numberBetween(0, 500))
                    .minusMinutes(faker.number().numberBetween(0, 1000));
                transaction.setDate(transactionDate);

                transactionRepository.save(transaction);

                int totalNumberOfTickets = ticketRepository.findAll().size();
                int numberOfTicketsToBuy = faker.number().numberBetween(1, 5);
                for (int j = 0; j < numberOfTicketsToBuy; j++) {
                    Ticket randomTicket = ticketRepository.getByTicketId(
                        faker.number().numberBetween(1L, totalNumberOfTickets));
                    if (randomTicket.getPurchasedBy() == null
                        && randomTicket.getReservedBy() == null) {
                        randomTicket.setPurchasedBy(user);
                        ticketRepository.save(randomTicket);

                        BookedIn bookedIn = new BookedIn();
                        bookedIn.setTransaction(transaction);
                        bookedIn.setTicket(randomTicket);
                        bookedIn.setBookingType(BookingType.PURCHASE);
                        // TODO: set price at booking time correctly
                        bookedIn.setPriceAtBookingTime(BigDecimal.valueOf(1));
                        bookedInRepository.save(bookedIn);
                    }
                }
            }
        }
    }
}
