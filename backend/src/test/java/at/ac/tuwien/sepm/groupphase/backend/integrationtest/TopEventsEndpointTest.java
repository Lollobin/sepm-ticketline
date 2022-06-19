package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithTicketsSoldDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class TopEventsEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql("classpath:/sql/insert_address.sql"),
        @Sql("classpath:/sql/insert_location.sql"),
        @Sql("classpath:/sql/insert_seatingPlanLayout.sql"),
        @Sql("classpath:/sql/insert_seatingPlan.sql"),
        @Sql("classpath:/sql/insert_sector.sql"),
        @Sql("classpath:/sql/insert_seat.sql"),
        @Sql("classpath:/sql/insert_user.sql"),
        @Sql("classpath:/sql/insert_event.sql"),
        @Sql("classpath:/sql/insert_show.sql"),
        @Sql("classpath:/sql/insert_sectorPrice.sql"),
        @Sql("classpath:/sql/insert_ticket.sql")
    })
    void topEventsGet_shouldReturnAllEvents_whenNoParameters() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/topEvents")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        MockHttpServletResponse response = result.getResponse();

        List<EventWithTicketsSoldDto> events = new ArrayList<>();
        events = objectMapper.readValue(response.getContentAsString(), List.class);

        assertThat(events).hasSize(2);

    }

    @Test
    void topEventsGet_shouldReturnCorrectEvents_whenValidParameters() {

    }

    @Test
    void topEventsGet_shouldReturnNoEvents_whenInvalidParameters() {

    }

    @Test
    void topEventsGet_shouldReturnCorrectAmountOfTicketsSold() {

    }

    private void saveEventsWithShowsAndTickets() {
        ADDRESS_ENTITY.setAddressId(null);
        ApplicationUser user = new ApplicationUser(USER_EMAIL, USER_FNAME, USER_LNAME, Gender.MALE,
            ADDRESS_ENTITY, ENCODED_USER_PASSWORD_EXAMPLE);
        userRepository.save(user);

        Event event1 = new Event();
        event1.setName(EVENT_NAME);
        event1.setContent(EVENT_CONTENT);
        event1.setCategory(EVENT_CATEGORY);
        event1.setDuration(EVENT_DURATION);
        eventRepository.save(event1);

/*
        Event event2 = new Event();
        event2.setName(EVENT2_NAME);
        event2.setContent(EVENT2_CONTENT);
        event2.setCategory(EVENT2_CATEGORY);
        event2.setDuration(EVENT2_DURATION);
        eventRepository.save(event2);

        Event event3 = new Event();
        event3.setName(EVENT3_NAME);
        event3.setContent(EVENT3_CONTENT);
        event3.setCategory(EVENT3_CATEGORY);
        event3.setDuration(EVENT3_DURATION);
        eventRepository.save(event3);
 */

        Show show1 = new Show();
        show1.setDate(OffsetDateTime.now());
        show1.setEvent(event1);
        showRepository.save(show1);

        Ticket ticket = new Ticket();
        ticket.setPurchasedBy(user);
        ticketRepository.save(ticket);

    }
}
