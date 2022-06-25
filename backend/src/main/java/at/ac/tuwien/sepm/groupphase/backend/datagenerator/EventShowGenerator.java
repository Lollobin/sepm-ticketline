package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Category;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        List<Show> shows = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        List<Artist> allArtists = artistRepository.findAll();
        for (int i = 0; i < numberOfEvents; i++) {
            Event event = generateEvent();
            LOGGER.trace("saving event {}", event);
            events.add(event);

            Artist mainArtist = allArtists.get(faker.number().numberBetween(1, allArtists.size()));

            int numberOfShows = faker.number().numberBetween(1, maxShowsPerEvent);
            for (int j = 0; j < numberOfShows; j++) {

                Map<Long, Artist> artists = new HashMap<>();
                artists.put(mainArtist.getArtistId(), mainArtist);

                double random = faker.number().randomDouble(3, 0, 1);
                if (random < 0.3) {
                    int numberOfAdditionalArtists = faker.number().numberBetween(1, 3);
                    for (int k = 0; k < numberOfAdditionalArtists; k++) {
                        Artist additionalArtist = allArtists.get(
                            faker.number().numberBetween(1, allArtists.size()));
                        artists.put(additionalArtist.getArtistId(), additionalArtist);
                    }
                }

                shows.add(generateShow(event, new HashSet<>(artists.values())));

            }

        }
        eventRepository.saveAll(events);
        showRepository.saveAll(shows);
    }

    private Event generateEvent() {
        Event event = new Event();
        if (faker.number().randomDouble(2, 0, 1) > 0.5) {
            event.setName(faker.harryPotter().location() + ": " + faker.esports().event());

        } else {
            event.setName(faker.starTrek().location() + " - " + faker.book().title());
        }
        event.setDuration(faker.number().numberBetween(1, 70) * 5L);
        event.setCategory(faker.options().option(Category.class));
        event.setContent(faker.lorem().paragraph(6));
        return event;
    }

    private Show generateShow(Event event, Set<Artist> artists) {
        Show show = new Show();
        OffsetDateTime date = OffsetDateTime.of(2022, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            .plusDays(faker.number().numberBetween(0, 364))
            .plusHours(faker.number().numberBetween(0, 23));
        show.setDate(date);
        show.setEvent(event);
        show.setArtists(artists);
        return show;
    }
}
