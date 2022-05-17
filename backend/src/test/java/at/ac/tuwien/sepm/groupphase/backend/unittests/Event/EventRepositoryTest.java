package at.ac.tuwien.sepm.groupphase.backend.unittests.Event;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void shouldSaveEventWithValidIdAndCorrectColumns(){
        Event testEvent = new Event();
        testEvent.setName(EVENT_NAME);
        testEvent.setDuration(EVENT_DURATION.longValue());
        testEvent.setContent(EVENT_CONTENT);
        testEvent.setCategory(EVENT_CATEGORY);

        Event event = eventRepository.save(testEvent);

        assertNotNull(testEvent.getEventId());
        assertThat(event).hasFieldOrPropertyWithValue("duration", EVENT_DURATION.longValue());
        assertThat(event).hasFieldOrPropertyWithValue("content", EVENT_CONTENT);
        assertThat(event).hasFieldOrPropertyWithValue("category", EVENT_CATEGORY);
        assertThat(event).hasFieldOrPropertyWithValue("name", EVENT_NAME);
    }

    @Test
    public void shouldStoreAllEvents(){
        Event event1 = new Event();
        Event event2 = new Event();
        Event event3 = new Event();

        event1.setName(EVENT_NAME);
        event1.setDuration(EVENT_DURATION.longValue());
        event1.setContent(EVENT_CONTENT);
        event1.setCategory(EVENT_CATEGORY);

        eventRepository.save(event1);

        event2.setName(EVENT2_NAME);
        event2.setDuration(EVENT2_DURATION.longValue());
        event2.setContent(EVENT2_CONTENT);
        event2.setCategory(EVENT2_CATEGORY);

        eventRepository.save(event2);

        event3.setName(EVENT3_NAME);
        event3.setDuration(EVENT3_DURATION.longValue());
        event3.setContent(EVENT3_CONTENT);
        event3.setCategory(EVENT3_CATEGORY);

        eventRepository.save(event3);

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(3).contains(event1, event2, event3);

    }

    @Test
    public void shouldFindEventById(){
        Event event2 = new Event();
        Event event3 = new Event();

        event2.setName(EVENT2_NAME);
        event2.setDuration(EVENT2_DURATION.longValue());
        event2.setContent(EVENT2_CONTENT);
        event2.setCategory(EVENT2_CATEGORY);

        eventRepository.save(event2);

        event3.setName(EVENT3_NAME);
        event3.setDuration(EVENT3_DURATION.longValue());
        event3.setContent(EVENT3_CONTENT);
        event3.setCategory(EVENT3_CATEGORY);

        eventRepository.save(event3);

        Event foundEvent = eventRepository.findById(event2.getEventId()).get();

        assertThat(foundEvent).isEqualTo(event2);
        assertThat(foundEvent.getName()).isEqualTo(EVENT2_NAME);
        assertThat(foundEvent.getDuration()).isEqualTo(EVENT2_DURATION.longValue());
        assertThat(foundEvent.getCategory()).isEqualTo(EVENT2_CATEGORY);
        assertThat(foundEvent.getContent()).isEqualTo(EVENT2_CONTENT);
    }
}
