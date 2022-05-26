package at.ac.tuwien.sepm.groupphase.backend.unittests.Show;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_LASTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_LASTNAME;
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

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@DataJpaTest
@ActiveProfiles("test")
class ShowRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void should_CreateNewShow_When_ShowIsValid() {
        Event testEvent = new Event();
        testEvent.setName(EVENT_NAME);
        testEvent.setDuration(EVENT_DURATION);
        testEvent.setContent(EVENT_CONTENT);
        testEvent.setCategory(EVENT_CATEGORY);

        Event event = eventRepository.save(testEvent);

        Artist testArtist = new Artist();
        testArtist.setFirstName(ARTIST_FIRSTNAME);
        testArtist.setLastName(ARTIST_LASTNAME);
        testArtist.setKnownAs(ARTIST_KNOWNAS);
        testArtist.setBandName(ARTIST_BANDNAME);

        Artist artist = artistRepository.save(testArtist);

        Artist testArtist2 = new Artist();
        testArtist.setFirstName(ARTIST2_FIRSTNAME);
        testArtist.setLastName(ARTIST2_LASTNAME);
        testArtist.setKnownAs(ARTIST2_KNOWNAS);
        testArtist.setBandName(ARTIST2_BANDNAME);

        Artist artist2 = artistRepository.save(testArtist2);

        Set<Artist> artists = new HashSet<>();
        artists.add(artist);
        artists.add(artist2);

        Show show1 = new Show();

        show1.setEvent(event);
        show1.setArtists(artists);
        show1.setDate(SHOW_DATE);

        showRepository.save(show1);

        assertNotNull(show1.getShowId());
        assertThat(show1).hasFieldOrPropertyWithValue("date", SHOW_DATE)
            .hasFieldOrPropertyWithValue("artists", show1.getArtists())
            .hasFieldOrPropertyWithValue("event", show1.getEvent());

        assertThat(show1.getEvent().getEventId()).isEqualTo(event.getEventId());
        assertThat(show1.getDate()).isEqualTo(SHOW_DATE);

        showRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();

    }

    @Test
    void should_FindShowById_When_ShowPresent() {

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
        artistRepository.deleteAll();

    }

    @Test
    void should_ReturnAllStoredShows_When_RepoNotEmpty() {


        showRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();

        saveThreeShowsAndEvents();
        List<Show> allShows = showRepository.findAll();

        assertThat(allShows).hasSize(3);
        assertThat(allShows.get(0).getEvent().getName()).isEqualTo(EVENT_NAME);
        assertThat(allShows.get(0).getEvent().getContent()).isEqualTo(EVENT_CONTENT);
        assertThat(allShows.get(0).getDate()).isEqualTo(SHOW_DATE);
        assertThat(allShows.get(0).getArtists()).isNotNull();

        assertThat(allShows.get(1).getEvent().getName()).isEqualTo(EVENT2_NAME);
        assertThat(allShows.get(1).getEvent().getContent()).isEqualTo(EVENT2_CONTENT);
        assertThat(allShows.get(1).getDate()).isEqualTo(SHOW2_DATE);
        assertThat(allShows.get(1).getArtists()).isNotNull();

        assertThat(allShows.get(2).getEvent().getDuration()).isEqualTo(EVENT3_DURATION);
        assertThat(allShows.get(2).getEvent().getContent()).isEqualTo(EVENT3_CONTENT);
        assertThat(allShows.get(2).getDate()).isEqualTo(SHOW3_DATE);
        assertThat(allShows.get(2).getArtists()).isNull();

        showRepository.deleteAll();
        eventRepository.deleteAll();
        artistRepository.deleteAll();
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

        Artist testArtist = new Artist();
        testArtist.setFirstName(ARTIST_FIRSTNAME);
        testArtist.setLastName(ARTIST_LASTNAME);
        testArtist.setKnownAs(ARTIST_KNOWNAS);
        testArtist.setBandName(ARTIST_BANDNAME);

        Artist artist = artistRepository.save(testArtist);

        Artist testArtist2 = new Artist();
        testArtist.setFirstName(ARTIST2_FIRSTNAME);
        testArtist.setLastName(ARTIST2_LASTNAME);
        testArtist.setKnownAs(ARTIST2_KNOWNAS);
        testArtist.setBandName(ARTIST2_BANDNAME);

        Artist artist2 = artistRepository.save(testArtist2);

        Set<Artist> artists1 = new HashSet<>();
        artists1.add(artist);

        Set<Artist> artists = new HashSet<>();
        artists.add(artist);
        artists.add(artist2);

        Show show1 = new Show();

        show1.setEvent(event1);
        show1.setArtists(artists);
        show1.setDate(SHOW_DATE);

        showRepository.save(show1);

        Show show2 = new Show();

        show2.setEvent(event2);
        show2.setArtists(artists1);
        show2.setDate(SHOW2_DATE);

        showRepository.save(show2);

        Show show3 = new Show();

        show3.setEvent(event3);
        show3.setArtists(null);
        show3.setDate(SHOW3_DATE);

        showRepository.save(show3);
    }
    @Test
    @SqlGroup({

        @Sql("classpath:/sql/delete.sql"),
        @Sql("classpath:/sql/insert_address.sql"),
        @Sql("classpath:/sql/insert_location.sql"),
        @Sql("classpath:/sql/insert_seatingPlanLayout.sql"),
        @Sql("classpath:/sql/insert_seatingPlan.sql"),
        @Sql("classpath:/sql/insert_sector.sql"),
        @Sql("classpath:/sql/insert_event.sql"),
        @Sql("classpath:/sql/insert_show.sql"),
        @Sql("classpath:/sql/insert_sectorPrice.sql"),
    })
    public void shouldTest(){

        String eventName = "gutes";


    }

    @Test
    @SqlGroup({
        @Sql("classpath:/sql/delete.sql"),
        @Sql("classpath:/sql/insert_address.sql"),
        @Sql("classpath:/sql/insert_location.sql"),
        @Sql("classpath:/sql/insert_seatingPlanLayout.sql"),
        @Sql("classpath:/sql/insert_seatingPlan.sql"),
        @Sql("classpath:/sql/insert_sector.sql"),
        @Sql("classpath:/sql/insert_event.sql"),
        @Sql("classpath:/sql/insert_show.sql"),
        @Sql("classpath:/sql/insert_sectorPrice.sql"),
    })
    public void shouldTesfet(){

        List event = eventRepository.findAll();

        List address = addressRepository.findAll();
        System.out.println(addressRepository.findAll());

        assertThat(address).hasSize(5);

    }

}
