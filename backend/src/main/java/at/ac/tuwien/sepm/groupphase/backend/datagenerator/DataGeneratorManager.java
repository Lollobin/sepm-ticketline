package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class DataGeneratorManager {

    private static final int NUMBER_OF_USERS = 2;
    private static final int NUMBER_OF_ARTISTS = 20;
    private static final int NUMBER_OF_EVENTS = 30;
    private static final int MAX_NUMBER_OF_SHOWS_PER_EVENT = 5;
    private static final int NUMBER_OF_LOCATIONS = 4;
    private static final int MAX_NUMBER_OF_SEATING_PLANS_PER_LOCATION = 3;

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserGenerator userGenerator;
    private final ArtistGenerator artistGenerator;
    private final EventShowGenerator eventShowGenerator;
    private final LocationGenerator locationGenerator;
    private final SeatDataGenerator seatDataGenerator;
    private final TicketSectorPriceGenerator ticketSectorPriceGenerator;
    private final TransactionBookedInGenerator transactionDataGenerator;

    public DataGeneratorManager(
        UserGenerator userGenerator,
        ArtistGenerator artistGenerator,
        EventShowGenerator eventShowGenerator,
        LocationGenerator locationGenerator,
        SeatDataGenerator seatDataGenerator,
        TicketSectorPriceGenerator ticketSectorPriceGenerator,
        TransactionBookedInGenerator transactionDataGenerator) {
        this.userGenerator = userGenerator;
        this.artistGenerator = artistGenerator;
        this.eventShowGenerator = eventShowGenerator;
        this.locationGenerator = locationGenerator;
        this.seatDataGenerator = seatDataGenerator;
        this.ticketSectorPriceGenerator = ticketSectorPriceGenerator;
        this.transactionDataGenerator = transactionDataGenerator;
    }

    @PostConstruct
    private void generateData() {
        LOGGER.debug("starting data generation");
        userGenerator.generateData(NUMBER_OF_USERS);
        artistGenerator.generateData(NUMBER_OF_ARTISTS);
        eventShowGenerator.generateData(NUMBER_OF_EVENTS, MAX_NUMBER_OF_SHOWS_PER_EVENT);
        locationGenerator.generateData(NUMBER_OF_LOCATIONS);
        try {
            seatDataGenerator.generateData(MAX_NUMBER_OF_SEATING_PLANS_PER_LOCATION);
        } catch (IOException e) {
            LOGGER.error("error generating seatingPlanLayouts", e);
        }
        ticketSectorPriceGenerator.generateData();
        transactionDataGenerator.generateData();
    }

}
