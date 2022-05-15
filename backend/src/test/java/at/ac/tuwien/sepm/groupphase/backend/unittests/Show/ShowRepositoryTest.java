package at.ac.tuwien.sepm.groupphase.backend.unittests.Show;

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
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW2_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW3_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_DATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class ShowRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ShowRepository showRepository;

    @Test
    public void shouldSaveNewEvent() {
        Event testEvent = new Event();
        testEvent.setName(EVENT_NAME);
        testEvent.setDuration(EVENT_DURATION);
        testEvent.setContent(EVENT_CONTENT);
        testEvent.setCategory(EVENT_CATEGORY);

        Event event = eventRepository.save(testEvent);

        Show show1 = new Show();

        show1.setEvent(event);
        show1.setArtists(null);
        show1.setDate(SHOW_DATE);

        showRepository.save(show1);

        assertNotNull(show1.getShowId());
        assertThat(show1).hasFieldOrPropertyWithValue("date", SHOW_DATE);
        assertThat(show1).hasFieldOrPropertyWithValue("artists", null);
        assertThat(show1).hasFieldOrPropertyWithValue("event", show1.getEvent());

        assertThat(show1.getEvent().getEventId()).isEqualTo(event.getEventId());
        assertThat(show1.getDate()).isEqualTo(SHOW_DATE);
        assertThat(show1.getArtists()).isEqualTo(null);

        showRepository.deleteAll();
        eventRepository.deleteAll();

    }

    @Test
    public void shouldFindShowById() {

        saveThreeShowsAndEvents();
        List<Show> allShows = showRepository.findAll();

        Show firstShow = allShows.get(0);

        Show returnedById = showRepository.findById(firstShow.getShowId()).get();

        assertThat(returnedById.getShowId()).isEqualTo(firstShow.getShowId());
        assertThat(returnedById.getDate()).isEqualTo(firstShow.getDate());
        assertThat(returnedById.getEvent()).isEqualTo(firstShow.getEvent());
        assertThat(returnedById.getArtists()).isEqualTo(firstShow.getArtists());

        showRepository.deleteAll();
        eventRepository.deleteAll();

    }

    @Test
    public void shouldReturnAllStoredShows() {

        saveThreeShowsAndEvents();
        List<Show> allShows = showRepository.findAll();

        assertThat(allShows).hasSize(3);
        assertThat(allShows.get(0).getEvent().getName()).isEqualTo(EVENT_NAME);
        assertThat(allShows.get(0).getEvent().getContent()).isEqualTo(EVENT_CONTENT);
        assertThat(allShows.get(0).getDate()).isEqualTo(SHOW_DATE);
        assertThat(allShows.get(0).getArtists()).isEqualTo(null);

        assertThat(allShows.get(1).getEvent().getName()).isEqualTo(EVENT2_NAME);
        assertThat(allShows.get(1).getEvent().getContent()).isEqualTo(EVENT2_CONTENT);
        assertThat(allShows.get(1).getDate()).isEqualTo(SHOW2_DATE);
        assertThat(allShows.get(1).getArtists()).isEqualTo(null);

        assertThat(allShows.get(2).getEvent().getDuration()).isEqualTo(EVENT3_DURATION);
        assertThat(allShows.get(2).getEvent().getContent()).isEqualTo(EVENT3_CONTENT);
        assertThat(allShows.get(2).getDate()).isEqualTo(SHOW3_DATE);
        assertThat(allShows.get(2).getArtists()).isEqualTo(null);

        showRepository.deleteAll();
        eventRepository.deleteAll();
    }

    private void saveThreeShowsAndEvents() {
        Event event1 = new Event();
        event1.setCategory(EVENT_CATEGORY);
        event1.setContent(EVENT_CONTENT);
        event1.setName(EVENT_NAME);
        event1.setDuration(EVENT_DURATION);

        eventRepository.save(event1);

        Event event2 = new Event();
        event2.setCategory(EVENT2_CATEGORY);
        event2.setContent(EVENT2_CONTENT);
        event2.setName(EVENT2_NAME);
        event2.setDuration(EVENT2_DURATION);

        eventRepository.save(event2);

        Event event3 = new Event();
        event3.setCategory(EVENT3_CATEGORY);
        event3.setContent(EVENT3_CONTENT);
        event3.setName(EVENT3_NAME);
        event3.setDuration(EVENT3_DURATION);

        eventRepository.save(event3);

        Show show1 = new Show();

        show1.setEvent(event1);
        show1.setArtists(null);
        show1.setDate(SHOW_DATE);

        showRepository.save(show1);

        Show show2 = new Show();

        show2.setEvent(event2);
        show2.setArtists(null);
        show2.setDate(SHOW2_DATE);

        showRepository.save(show2);

        Show show3 = new Show();

        show3.setEvent(event3);
        show3.setArtists(null);
        show3.setDate(SHOW3_DATE);

        showRepository.save(show3);
    }

}
