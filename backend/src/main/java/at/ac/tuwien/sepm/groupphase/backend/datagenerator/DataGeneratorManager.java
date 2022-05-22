package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import java.lang.invoke.MethodHandles;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class DataGeneratorManager {

    private static final int NUMBER_OF_USERS = 20;
    private static final int NUMBER_OF_ARTISTS = 50;
    private static final int NUMBER_OF_EVENTS = 30;
    private static final int MAX_NUMBER_OF_SHOWS_PER_EVENT = 10;

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserDataGenerator userDataGenerator;
    private final ArtistDataGenerator artistDataGenerator;
    private final EventDataGenerator eventDataGenerator;
    private final ShowDataGenerator showDataGenerator;

    public DataGeneratorManager(
        UserDataGenerator userDataGenerator,
        ArtistDataGenerator artistDataGenerator,
        EventDataGenerator eventDataGenerator,
        ShowDataGenerator showDataGenerator) {
        this.userDataGenerator = userDataGenerator;
        this.artistDataGenerator = artistDataGenerator;
        this.eventDataGenerator = eventDataGenerator;
        this.showDataGenerator = showDataGenerator;

    }

    @PostConstruct
    private void generateData() {
        LOGGER.debug("starting data generation");
        userDataGenerator.generateUsers(NUMBER_OF_USERS);
        artistDataGenerator.generateArtists(NUMBER_OF_ARTISTS);
        eventDataGenerator.generateEvents(NUMBER_OF_EVENTS);
        showDataGenerator.generateShows(MAX_NUMBER_OF_SHOWS_PER_EVENT);
    }

}
