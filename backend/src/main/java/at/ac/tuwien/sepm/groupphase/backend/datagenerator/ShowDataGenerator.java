package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//TODO: Replace with proper dataGenerator class and create dataGenerator for users and addresses
@Profile("generateData")
@Component
public class ShowDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final ShowRepository showRepository;
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;

    public ShowDataGenerator(ShowRepository showRepository, ArtistRepository artistRepository,
        EventRepository eventRepository) {
        this.showRepository = showRepository;
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
    }

    @PostConstruct
    private void generateData() {
        if (!showRepository.findAll().isEmpty()) {
            LOGGER.debug("order already generated");
            return;
        }
        Artist a1 = new Artist();
        a1.setArtistId(1);
        a1.setFirstName("Al");
        a1.setLastName("Yankovic");
        a1.setKnownAs("Weird Al");
        artistRepository.save(a1);

        Event e1 = new Event();
        e1.setEventId(1);
        e1.setDuration(120);
        e1.setCategory("POP");
        e1.setName("Weird Al's cool tour");
        eventRepository.save(e1);

        Show show = new Show();
        show.setShowId(1);
        show.setArtists(Set.of(a1));
        show.setDate(OffsetDateTime.now());
        show.setEvent(e1);

        showRepository.save(show);
    }
}
