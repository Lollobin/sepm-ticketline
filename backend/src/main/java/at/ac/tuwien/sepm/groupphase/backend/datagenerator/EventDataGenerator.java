package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class EventDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final EventRepository eventRepository;
    private final Faker faker = new Faker();

    public EventDataGenerator(
        EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void generateEvents(int numberOfEvents) {
        if (!eventRepository.findAll().isEmpty()) {
            LOGGER.debug("events already generated");
            return;
        }

        LOGGER.debug("generating {} events", numberOfEvents);

        for (int i = 0; i < numberOfEvents; i++) {
            Event event = new Event();
            event.setName(faker.harryPotter().location());
            event.setDuration(faker.number().numberBetween(5, 50) * 10L);
            // TODO: find way to generate category. Consider changing it to enum
            event.setCategory("");
            event.setContent(faker.lorem().paragraph(3));
            LOGGER.debug("saving event {}", event);
            eventRepository.save(event);
        }
    }
}
