package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class ShowDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;
    private final Faker faker = new Faker();

    public ShowDataGenerator(ShowRepository showRepository,
        EventRepository eventRepository,
        ArtistRepository artistRepository) {
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
    }

    public void generateShows(int maxShowsPerEvent) {

        if (!showRepository.findAll().isEmpty()) {
            LOGGER.debug("shows already generated");
            return;
        }

        LOGGER.debug("generating shows for each event");
        List<Event> events = eventRepository.findAll();
        for (Event event : events) {

            // TODO: add possibility to have multiple artists
            int numberOfArtists = artistRepository.findAll().size();

            Artist randomArtist = artistRepository.getByArtistId(
                (long) faker.number().numberBetween(1, numberOfArtists));

            Set<Artist> artists = new HashSet<>();
            artists.add(randomArtist);

            int numberOfShows = faker.number().numberBetween(1, maxShowsPerEvent);

            for (int i = 0; i < numberOfShows; i++) {
                Show show = new Show();

                OffsetDateTime date = OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
                    .plusYears(faker.number().numberBetween(0, 6))
                    .plusDays(faker.number().numberBetween(0, 364))
                    .plusHours(faker.number().numberBetween(0, 23));
                show.setDate(date);
                show.setEvent(event);
                show.setArtists(artists);

                LOGGER.debug("saving show {}", show);
                showRepository.save(show);
            }
        }
    }
}
