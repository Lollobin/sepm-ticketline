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
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW2_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW3_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_ROLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class ShowEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void setup() {
        showRepository.deleteAll();
        eventRepository.deleteAll();


    }

    @Test
    public void shouldReturnAllStoredShows() throws Exception {

        saveThreeShowsAndEvents();

        MvcResult mvcResult =
            this.mockMvc
                .perform(
                    get("/shows")
                        .header(
                            securityProperties.getAuthHeader(),
                            jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ShowDto> showDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(), ShowDto[].class));


        assertThat(showDtos).hasSize(3);
        assertThat(showDtos.get(0).getDate()).isEqualTo(SHOW_DATE);
        assertThat(showDtos.get(1).getDate()).isEqualTo(SHOW2_DATE);
        assertThat(showDtos.get(2).getDate()).isEqualTo(SHOW3_DATE);


    }

    @Test
    public void shouldReturnShowById() throws Exception {

        saveThreeShowsAndEvents();

        List<Show> allShows = showRepository.findAll();

        Show firstShow = allShows.get(0);

        MvcResult mvcResult = this.mockMvc
            .perform(
                get("/shows/{id}", firstShow.getShowId())
                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        ShowDto showDto = objectMapper.readValue(response.getContentAsString(), ShowDto.class);

        assertThat(showDto.getDate()).isEqualTo(firstShow.getDate());
        assertThat(showDto.getShowId().longValue()).isEqualTo(firstShow.getShowId());

    }

    @Test
    public void shouldReturn404DueToNotPresentShow() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/events/-100")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

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
