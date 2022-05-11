package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithoutIdDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@EnableWebMvc
@WebAppConfiguration
public class EventEndpointTest {
    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    @Transactional
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void firstTestShouldReturnSize0() throws Exception {
        byte[] body = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/events")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        List<EventDto> eventResult = objectMapper.readerFor(EventDto.class).<EventDto>readValues(body).readAll();

        assertThat(eventResult).isNotNull();
        assertThat(eventResult.size()).isEqualTo(1);
        assertThat(eventResult.get(0).getName()).isEqualTo(EVENT_NAME);
        assertThat(eventResult.get(0).getDuration()).isEqualTo(EVENT_DURATION);
        assertThat(eventResult.get(0).getCategory()).isEqualTo(EVENT_CATEGORY);
        assertThat(eventResult.get(0).getContent()).isEqualTo(EVENT_CONTENT);

    }

    @Test
    public void shouldSaveAEventWithCode201() throws Exception {

        EventWithoutIdDto eventWithoutIdToSave = new EventWithoutIdDto();
        eventWithoutIdToSave.setName(EVENT_NAME);
        eventWithoutIdToSave.setDuration(EVENT_DURATION);
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);

        String json = objectMapper.writeValueAsString(eventWithoutIdToSave);

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/events")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void invalidJSONShouldReturnCode422() throws Exception {

        EventWithoutIdDto eventWithoutIdToSave = new EventWithoutIdDto();
        eventWithoutIdToSave.setName(EVENT_INVALID_NAME);
        eventWithoutIdToSave.setDuration(EVENT_INVALID_DURATION_LOWER);
        eventWithoutIdToSave.setCategory(EVENT_CATEGORY);
        eventWithoutIdToSave.setContent(EVENT_CONTENT);

        String json = objectMapper.writeValueAsString(eventWithoutIdToSave);

        mockMvc.perform( MockMvcRequestBuilders
                        .post("/events")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity());

    }



}

