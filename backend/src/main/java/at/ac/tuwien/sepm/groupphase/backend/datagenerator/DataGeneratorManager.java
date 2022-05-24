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

    private static final int NUMBER_OF_USERS = 5;
    private static final int NUMBER_OF_ARTISTS = 20;
    private static final int NUMBER_OF_EVENTS = 10;
    private static final int MAX_NUMBER_OF_SHOWS_PER_EVENT = 5;
    private static final int NUMBER_OF_LOCATIONS = 3;
    private static final int MAX_NUMBER_OF_SEATING_PLANS_PER_LOCATION = 5;

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserGenerator userGenerator;
    private final ArtistGenerator artistGenerator;
    private final EventShowGenerator eventShowGenerator;
    private final LocationGenerator locationGenerator;
    private final SeatingPlanLayoutGenerator seatingPlanLayoutGenerator;
    private final SeatingPlanGenerator seatingPlanGenerator;
    private final SectorGenerator sectorGenerator;
    private final SeatGenerator seatGenerator;
    private final TicketSectorPriceGenerator ticketSectorPriceGenerator;
    private final TransactionBookedInGenerator transactionDataGenerator;

    public DataGeneratorManager(
        UserGenerator userGenerator,
        ArtistGenerator artistGenerator,
        EventShowGenerator eventShowGenerator,
        LocationGenerator locationGenerator,
        SeatingPlanLayoutGenerator seatingPlanLayoutGenerator,
        SeatingPlanGenerator seatingPlanGenerator,
        SectorGenerator sectorGenerator,
        SeatGenerator seatGenerator,
        TicketSectorPriceGenerator ticketSectorPriceGenerator,
        TransactionBookedInGenerator transactionDataGenerator) {
        this.userGenerator = userGenerator;
        this.artistGenerator = artistGenerator;
        this.eventShowGenerator = eventShowGenerator;
        this.locationGenerator = locationGenerator;
        this.seatingPlanLayoutGenerator = seatingPlanLayoutGenerator;
        this.seatingPlanGenerator = seatingPlanGenerator;
        this.sectorGenerator = sectorGenerator;
        this.seatGenerator = seatGenerator;
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
            seatingPlanLayoutGenerator.generateSeatingPlanLayouts();
        } catch (IOException e) {
            LOGGER.error("error generating seatingPlanLayouts", e);
        }
        seatingPlanGenerator.generateSeatingPlans(MAX_NUMBER_OF_SEATING_PLANS_PER_LOCATION);
        sectorGenerator.generateSectors();
        seatGenerator.generateSeats();

        ticketSectorPriceGenerator.generateData();
        transactionDataGenerator.generateData();
    }

}
