package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.DEFAULT_USER;
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
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_DURATION_LOWER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_INVALID_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_ROLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class EventsEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void setup() {
        eventRepository.deleteAll();
    }

    @Test
    void should_ReturnAllStoredEvents_When_RepoNotEmpty() throws Exception {

        saveThreeValidEvents();

        byte[] body = mockMvc
            .perform(MockMvcRequestBuilders
                .get("/events")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsByteArray();

        EventSearchResultDto resultDto = objectMapper.readerFor(EventSearchResultDto.class)
            .<EventSearchResultDto>readValues(body).readAll().get(0);

        List<EventDto> eventResult = resultDto.getEvents();

        assertThat(eventResult).hasSize(3);

        assertThat(eventResult.get(0).getName()).isEqualTo(EVENT2_NAME);
        assertThat(eventResult.get(0).getDuration().longValue()).isEqualTo(EVENT2_DURATION);
        assertThat(eventResult.get(0).getCategory()).isEqualTo(EVENT2_CATEGORY);
        assertThat(eventResult.get(0).getContent()).isEqualTo(EVENT2_CONTENT);

        assertThat(eventResult.get(1).getName()).isEqualTo(EVENT3_NAME);
        assertThat(eventResult.get(1).getDuration().longValue()).isEqualTo(EVENT3_DURATION);
        assertThat(eventResult.get(1).getCategory()).isEqualTo(EVENT3_CATEGORY);
        assertThat(eventResult.get(1).getContent()).isEqualTo(EVENT3_CONTENT);

        assertThat(eventResult.get(2).getName()).isEqualTo(EVENT_NAME);
        assertThat(eventResult.get(2).getDuration().longValue()).isEqualTo(EVENT_DURATION);
        assertThat(eventResult.get(2).getCategory()).isEqualTo(EVENT_CATEGORY);
        assertThat(eventResult.get(2).getContent()).isEqualTo(EVENT_CONTENT);
    }

    @Test
    void should_createEventWithResponse201AndLocationHeader_When_EventDtoIsValid()
        throws Exception {

        eventRepository.deleteAll();
        EventWithoutIdDto eventWithoutIdToSave = new EventWithoutIdDto();
        eventWithoutIdToSave.setName(EVENT_NAME);
        eventWithoutIdToSave.setDuration(BigDecimal.valueOf(EVENT_DURATION));
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);

        String json = objectMapper.writeValueAsString(eventWithoutIdToSave);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

        List<Event> savedEvents = eventRepository.findAll();
        assertThat(savedEvents).hasSize(1);
        assertThat(savedEvents.get(0).getName()).isEqualTo(EVENT_NAME);
        Long id = savedEvents.get(0).getEventId();

        MockHttpServletResponse response = result.getResponse();
        assertThat(response.getStatus()).isEqualTo(201);
        assertThat(response.getRedirectedUrl()).isEqualTo("http://localhost/events/" + id);


    }

    @Test
    void should_ReturnStatus403_When_RoleIsInvalid() throws Exception {

        EventWithoutIdDto eventWithoutIdToSave = new EventWithoutIdDto();
        eventWithoutIdToSave.setName(EVENT_NAME);
        eventWithoutIdToSave.setDuration(BigDecimal.valueOf(EVENT_DURATION));
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);

        String json = objectMapper.writeValueAsString(eventWithoutIdToSave);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void should_ReturnEventById_When_EventIsPresent() throws Exception {

        saveThreeValidEvents();

        List<Event> allEvents = eventRepository.findAll();

        Event firstEvent = allEvents.get(0);

        MvcResult mvcResult = this.mockMvc
            .perform(
                get("/events/{id}", firstEvent.getEventId())
                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        EventDto eventDto = objectMapper.readValue(response.getContentAsString(), EventDto.class);

        assertThat(eventDto.getContent()).isEqualTo(firstEvent.getContent());
        assertThat(eventDto.getCategory()).isEqualTo(firstEvent.getCategory());
        assertThat(eventDto.getEventId().longValue()).isEqualTo(firstEvent.getEventId());
        assertThat(eventDto.getName()).isEqualTo(firstEvent.getName());
    }

    @Test
    void should_Return404_When_EventNotPresent() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/events/-100")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    void should_Return422_When_JsonIsInvalid() throws Exception {

        EventWithoutIdDto eventWithoutIdToSave = new EventWithoutIdDto();
        eventWithoutIdToSave.setName(EVENT_INVALID_NAME);
        eventWithoutIdToSave.setDuration(BigDecimal.valueOf(EVENT_INVALID_DURATION_LOWER));
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);

        String json = objectMapper.writeValueAsString(eventWithoutIdToSave);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/events")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void getByName_shouldReturnCorrectEvent() throws Exception {

        saveThreeValidEvents();

        String name = EVENT_NAME;

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/events").param("name", name)
        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        EventSearchResultDto eventSearchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), EventSearchResultDto.class);

        assertThat(eventSearchResultDto.getEvents()).hasSize(1);
        assertThat(eventSearchResultDto.getEvents().get(0).getName()).isEqualTo(EVENT_NAME);
        assertThat(eventSearchResultDto.getEvents().get(0).getCategory()).isEqualTo(EVENT_CATEGORY);
        assertThat(eventSearchResultDto.getEvents().get(0).getContent()).isEqualTo(EVENT_CONTENT);
        assertThat(eventSearchResultDto.getEvents().get(0).getDuration()).isEqualTo(BigDecimal.valueOf(EVENT_DURATION));
    }

    @Test
    void getByContent_shouldReturnCorrectEvents() throws Exception {

        saveThreeValidEvents();

        String content = "is a";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/events").param("content", content)
        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        EventSearchResultDto eventSearchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), EventSearchResultDto.class);

        assertThat(eventSearchResultDto.getEvents()).hasSize(2);
        assertThat(eventSearchResultDto.getEvents().get(0).getName()).isEqualTo(EVENT2_NAME);
        assertThat(eventSearchResultDto.getEvents().get(0).getCategory()).isEqualTo(EVENT2_CATEGORY);
        assertThat(eventSearchResultDto.getEvents().get(0).getContent()).isEqualTo(EVENT2_CONTENT);
        assertThat(eventSearchResultDto.getEvents().get(0).getDuration()).isEqualTo(BigDecimal.valueOf(EVENT2_DURATION));
        assertThat(eventSearchResultDto.getEvents().get(1).getName()).isEqualTo(EVENT3_NAME);
        assertThat(eventSearchResultDto.getEvents().get(1).getCategory()).isEqualTo(EVENT3_CATEGORY);
        assertThat(eventSearchResultDto.getEvents().get(1).getContent()).isEqualTo(EVENT3_CONTENT);
        assertThat(eventSearchResultDto.getEvents().get(1).getDuration()).isEqualTo(BigDecimal.valueOf(EVENT3_DURATION));
    }

    @Test
    void getByCategoryAndDuration_shouldReturnCorrectEvent() throws Exception {

        saveThreeValidEvents();

        String category = EVENT3_CATEGORY;
        String duration = "20";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/events").param("category", category).param("duration", duration)
        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        EventSearchResultDto eventSearchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), EventSearchResultDto.class);

        assertThat(eventSearchResultDto.getEvents()).hasSize(1);
        assertThat(eventSearchResultDto.getEvents().get(0).getName()).isEqualTo(EVENT3_NAME);
        assertThat(eventSearchResultDto.getEvents().get(0).getCategory()).isEqualTo(EVENT3_CATEGORY);
        assertThat(eventSearchResultDto.getEvents().get(0).getContent()).isEqualTo(EVENT3_CONTENT);
        assertThat(eventSearchResultDto.getEvents().get(0).getDuration()).isEqualTo(BigDecimal.valueOf(EVENT3_DURATION));
    }

    @Test
    void getByIncorrectName_shouldReturnNoEvents() throws Exception {

        saveThreeValidEvents();

        String name = "INVALID NAME";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/events").param("name", name)
        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        EventSearchResultDto eventSearchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), EventSearchResultDto.class);

        assertThat(eventSearchResultDto.getEvents()).isEmpty();
    }

    private void saveThreeValidEvents() {
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
    }
}

