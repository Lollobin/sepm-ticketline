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

    private final UserDataGenerator userDataGenerator;
    private final ArtistDataGenerator artistDataGenerator;
    private final EventDataGenerator eventDataGenerator;
    private final ShowDataGenerator showDataGenerator;
    private final LocationDataGenerator locationDataGenerator;
    private final SeatingPlanLayoutDataGenerator seatingPlanLayoutDataGenerator;
    private final SeatingPlanDataGenerator seatingPlanDataGenerator;
    private final SectorDataGenerator sectorDataGenerator;
    private final SeatDataGenerator seatDataGenerator;
    private final TicketDataGenerator ticketDataGenerator;
    private final TransactionDataGenerator transactionDataGenerator;

    public DataGeneratorManager(
        UserDataGenerator userDataGenerator,
        ArtistDataGenerator artistDataGenerator,
        EventDataGenerator eventDataGenerator,
        ShowDataGenerator showDataGenerator,
        LocationDataGenerator locationDataGenerator,
        SeatingPlanLayoutDataGenerator seatingPlanLayoutDataGenerator,
        SeatingPlanDataGenerator seatingPlanDataGenerator,
        SectorDataGenerator sectorDataGenerator,
        SeatDataGenerator seatDataGenerator,
        TicketDataGenerator ticketDataGenerator,
        TransactionDataGenerator transactionDataGenerator) {
        this.userDataGenerator = userDataGenerator;
        this.artistDataGenerator = artistDataGenerator;
        this.eventDataGenerator = eventDataGenerator;
        this.showDataGenerator = showDataGenerator;
        this.locationDataGenerator = locationDataGenerator;
        this.seatingPlanLayoutDataGenerator = seatingPlanLayoutDataGenerator;
        this.seatingPlanDataGenerator = seatingPlanDataGenerator;
        this.sectorDataGenerator = sectorDataGenerator;
        this.seatDataGenerator = seatDataGenerator;
        this.ticketDataGenerator = ticketDataGenerator;
        this.transactionDataGenerator = transactionDataGenerator;
    }

    @PostConstruct
    private void generateData() {
        LOGGER.debug("starting data generation");
        userDataGenerator.generateUsers(NUMBER_OF_USERS);
        artistDataGenerator.generateArtists(NUMBER_OF_ARTISTS);
        eventDataGenerator.generateEvents(NUMBER_OF_EVENTS);
        showDataGenerator.generateShows(MAX_NUMBER_OF_SHOWS_PER_EVENT);
        locationDataGenerator.generateLocations(NUMBER_OF_LOCATIONS);
        try {
            seatingPlanLayoutDataGenerator.generateSeatingPlanLayouts();
        } catch (IOException e) {
            LOGGER.error("error generating seatingPlanLayouts", e);
        }
        seatingPlanDataGenerator.generateSeatingPlans(MAX_NUMBER_OF_SEATING_PLANS_PER_LOCATION);
        sectorDataGenerator.generateSectors();
        seatDataGenerator.generateSeats();
        ticketDataGenerator.generateTickets();
        transactionDataGenerator.generateTransactions();
    }

}
