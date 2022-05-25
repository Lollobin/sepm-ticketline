package at.ac.tuwien.sepm.groupphase.backend.unittests.Event;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT2_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT3_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void should_CreateNewEvent_When_EventIsValid() {
        Event testEvent = new Event();
        testEvent.setName(EVENT_NAME);
        testEvent.setDuration(EVENT_DURATION);
        testEvent.setContent(EVENT_CONTENT);
        testEvent.setCategory(EVENT_CATEGORY);

        Event event = eventRepository.save(testEvent);

        assertNotNull(testEvent.getEventId());
        assertThat(event).hasFieldOrPropertyWithValue("duration", EVENT_DURATION);
        assertThat(event).hasFieldOrPropertyWithValue("content", EVENT_CONTENT);
        assertThat(event).hasFieldOrPropertyWithValue("category", EVENT_CATEGORY);
        assertThat(event).hasFieldOrPropertyWithValue("name", EVENT_NAME);
    }

    @Test
    void should_CreateAllEvents_When_EventAreValid() {
        Event event1 = new Event();
        Event event2 = new Event();
        Event event3 = new Event();

        event1.setName(EVENT_NAME);
        event1.setDuration(EVENT_DURATION);
        event1.setContent(EVENT_CONTENT);
        event1.setCategory(EVENT_CATEGORY);

        eventRepository.save(event1);

        event2.setName(EVENT2_NAME);
        event2.setDuration(EVENT2_DURATION);
        event2.setContent(EVENT2_CONTENT);
        event2.setCategory(EVENT2_CATEGORY);

        eventRepository.save(event2);

        event3.setName(EVENT3_NAME);
        event3.setDuration(EVENT3_DURATION);
        event3.setContent(EVENT3_CONTENT);
        event3.setCategory(EVENT3_CATEGORY);

        eventRepository.save(event3);

        List<Event> events = eventRepository.findAll();
        assertThat(events).hasSize(3).contains(event1, event2, event3);

    }

    @Test
    void should_FindEventById_When_EventIsPresent() {
        Event event2 = new Event();
        Event event3 = new Event();

        event2.setName(EVENT2_NAME);
        event2.setDuration(EVENT2_DURATION);
        event2.setContent(EVENT2_CONTENT);
        event2.setCategory(EVENT2_CATEGORY);

        eventRepository.save(event2);

        event3.setName(EVENT3_NAME);
        event3.setDuration(EVENT3_DURATION);
        event3.setContent(EVENT3_CONTENT);
        event3.setCategory(EVENT3_CATEGORY);

        eventRepository.save(event3);

        Event foundEvent = eventRepository.findById(event2.getEventId()).get();

        assertThat(foundEvent).isEqualTo(event2);
        assertThat(foundEvent.getName()).isEqualTo(EVENT2_NAME);
        assertThat(foundEvent.getDuration()).isEqualTo(EVENT2_DURATION);
        assertThat(foundEvent.getCategory()).isEqualTo(EVENT2_CATEGORY);
        assertThat(foundEvent.getContent()).isEqualTo(EVENT2_CONTENT);
    }

    @Test
    void searchWithCorrectParams_shouldReturnCorrectEvents(){

        Event event = new Event();
        event.setName(EVENT_NAME);
        event.setContent(EVENT_CONTENT);
        event.setCategory(EVENT_CATEGORY);
        event.setDuration(EVENT_DURATION);

        Event event2 = new Event();
        event.setName(EVENT2_NAME);
        event.setContent(EVENT2_CONTENT);
        event.setCategory(EVENT2_CATEGORY);
        event.setDuration(EVENT2_DURATION);

        Event event3 = new Event();
        event.setName(EVENT3_NAME);
        event.setContent(EVENT3_CONTENT);
        event.setCategory(EVENT3_CATEGORY);
        event.setDuration(EVENT3_DURATION);

        eventRepository.save(event);
        eventRepository.save(event2);
        eventRepository.save(event3);

        assertNotNull(event.getEventId());
        assertThat(event).hasFieldOrPropertyWithValue("name", EVENT_NAME);
    }


}
