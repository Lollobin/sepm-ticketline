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
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class EventShowGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EventRepository eventRepository;
    private final ShowRepository showRepository;
    private final ArtistRepository artistRepository;
    private final Faker faker = new Faker();

    public EventShowGenerator(
        EventRepository eventRepository,
        ShowRepository showRepository,
        ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.showRepository = showRepository;
        this.artistRepository = artistRepository;
    }

    public void generateData(int numberOfEvents, int maxShowsPerEvent) {
        if (!eventRepository.findAll().isEmpty()) {
            LOGGER.debug("events already generated");
            return;
        }

        LOGGER.debug("generating {} events with shows", numberOfEvents);

        for (int i = 0; i < numberOfEvents; i++) {
            Event event = generateEvent();
            LOGGER.trace("saving event {}", event);
            eventRepository.save(event);

            // TODO: add possibility to have multiple artists
            int numberOfArtists = artistRepository.findAll().size();
            Artist randomArtist = artistRepository.getByArtistId(
                (long) faker.number().numberBetween(1, numberOfArtists));
            Set<Artist> artists = new HashSet<>();
            artists.add(randomArtist);

            int numberOfShows = faker.number().numberBetween(1, maxShowsPerEvent);
            for (int j = 0; j < numberOfShows; j++) {
                Show show = generateShow(event, artists);
                LOGGER.trace("saving show {}", show);
                showRepository.save(show);
            }

        }
    }

    private Event generateEvent() {
        Event event = new Event();
        event.setName(faker.harryPotter().location());
        event.setDuration(faker.number().numberBetween(5, 50) * 10L);
        // TODO: find way to generate category. Consider changing it to enum
        event.setCategory("");
        event.setContent(faker.lorem().paragraph(3));
        return event;
    }

    private Show generateShow(Event event, Set<Artist> artists) {
        Show show = new Show();
        OffsetDateTime date = OffsetDateTime.of(2018, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            .plusYears(faker.number().numberBetween(0, 6))
            .plusDays(faker.number().numberBetween(0, 364))
            .plusHours(faker.number().numberBetween(0, 23));
        show.setDate(date);
        show.setEvent(event);
        show.setArtists(artists);
        return show;
    }
}
