package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CategoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.EventWithTicketsSoldDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TopEventSearchDto;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
    void topEventsGet_shouldReturnAllEventsWithTicketsSold_whenNoParameters() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/topEvents")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        MockHttpServletResponse response = result.getResponse();

        List<EventWithTicketsSoldDto> events = objectMapper.readValue(response.getContentAsString(),
            new TypeReference<>() {
            });

        assertAll(
            () -> assertThat(events).hasSize(4),
            () -> assertThat(events.get(0).getEventId()).isEqualTo(-2),
            () -> assertThat(events.get(0).getTicketsSold()).isEqualTo(3),
            () -> assertThat(events.get(1).getEventId()).isEqualTo(-1),
            () -> assertThat(events.get(1).getTicketsSold()).isEqualTo(2)
        );

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
    void topEventsGet_shouldReturnCorrectEvents_whenValidParameters() throws Exception {

        TopEventSearchDto searchDto = new TopEventSearchDto().category(CategoryDto.POP).month(
            LocalDate.of(2022, 8, 1));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/topEvents")
                .param("month", "2022-08-01")
                .param("category", "POP")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
        MockHttpServletResponse response = result.getResponse();

        List<EventWithTicketsSoldDto> events = objectMapper.readValue(response.getContentAsString(),
            new TypeReference<>() {
            });

        assertAll(
            () -> assertThat(events).hasSize(1),
            () -> assertThat(events.get(0).getEventId()).isEqualTo(-1),
            () -> assertThat(events.get(0).getTicketsSold()).isEqualTo(2)
        );
    }
}
