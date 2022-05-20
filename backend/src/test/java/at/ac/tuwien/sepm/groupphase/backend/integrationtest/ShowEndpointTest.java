package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS_ENTITY;
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
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEATINGPLANLAYOUT_PATH;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEAT_ID1;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEAT_ID2;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEAT_ID3;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SECTOR_ID1;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SECTOR_ID2;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW2_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW3_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_DATE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_ROLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorPriceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ShowEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private SeatingPlanLayoutRepository seatingPlanLayoutRepository;

    @Autowired
    private SeatingPlanRepository seatingPlanRepository;

    @Autowired
    private SectorPriceRepository sectorPriceRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SectorRepository sectorRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void setup() {
        ticketRepository.deleteAll();
        seatRepository.deleteAll();
        sectorPriceRepository.deleteAll();
        showRepository.deleteAll();
        eventRepository.deleteAll();
        sectorRepository.deleteAll();
        seatingPlanRepository.deleteAll();
        seatingPlanLayoutRepository.deleteAll();
        locationRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void should_ReturnAllStoredShows_When_RepoNotEmpty() throws Exception {

        saveThreeShowsAndEvents();

        MvcResult mvcResult =
            this.mockMvc
                .perform(
                    get("/shows")
                        .header(
                            securityProperties.getAuthHeader(),
                            jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ShowDto> showDtos = Arrays.asList(
            objectMapper.readValue(response.getContentAsString(), ShowDto[].class));

        assertThat(showDtos).hasSize(3);
        assertThat(showDtos.get(0).getDate()).isEqualTo(SHOW_DATE);
        assertThat(showDtos.get(0).getShowId()).isEqualTo(1);
        assertThat(showDtos.get(1).getDate()).isEqualTo(SHOW2_DATE);
        assertThat(showDtos.get(1).getShowId()).isEqualTo(2);
        assertThat(showDtos.get(2).getDate()).isEqualTo(SHOW3_DATE);
        assertThat(showDtos.get(2).getShowId()).isEqualTo(3);


    }

    @Test
    void should_ReturnShowById_When_ShowIsPresent() throws Exception {

        saveThreeShowsAndEvents();

        List<Show> allShows = showRepository.findAll();

        Show firstShow = allShows.get(0);

        MvcResult mvcResult = this.mockMvc
            .perform(
                get("/shows/{id}", firstShow.getShowId())
                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        ShowDto showDto = objectMapper.readValue(response.getContentAsString(), ShowDto.class);

        assertThat(showDto.getDate()).isEqualTo(firstShow.getDate());
        assertThat(showDto.getShowId().longValue()).isEqualTo(firstShow.getShowId());

    }

    @Test
    void shouldReturn404DueToNotPresentShow() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/shows/-100")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

    }

    @Test
    void should_Return403_When_RoleIsInvalid() throws Exception {

        Event event1 = new Event();
        event1.setCategory(EVENT_CATEGORY);
        event1.setContent(EVENT_CONTENT);
        event1.setName(EVENT_NAME);
        event1.setDuration(EVENT_DURATION);

        eventRepository.save(event1);

        Address address = ADDRESS_ENTITY;
        address = addressRepository.save(address);

        Location location = new Location();
        location.setName("Filip's Cool Location");
        location.setAddress(address);
        locationRepository.save(location);

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        seatingPlanLayout.setSeatingLayoutPath(SEATINGPLANLAYOUT_PATH);
        seatingPlanLayoutRepository.save(seatingPlanLayout);

        SeatingPlan seatingPlan = new SeatingPlan();
        seatingPlan.setName("Filip's Corresponding Seating Plan");
        seatingPlan.setLocation(location);
        seatingPlan.setSeatingPlanLayout(seatingPlanLayout);
        seatingPlanRepository.save(seatingPlan);

        ShowWithoutIdDto showDtoToSave = new ShowWithoutIdDto();
        showDtoToSave.setDate(SHOW_DATE);
        showDtoToSave.setEvent(event1.getEventId().intValue());
        showDtoToSave.setSeatingPlan(Math.toIntExact(seatingPlan.getSeatingPlanId()));

        String json = objectMapper.writeValueAsString(showDtoToSave);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
            .post("/shows")
            .header(
                securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(DEFAULT_USER, USER_ROLES))
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

        assertThat(showRepository.findAll()).isEmpty();

    }

    @Test
    void should_CreateNewShow_When_ShowDtoIsValid() throws Exception {

        Event event1 = new Event();
        event1.setCategory(EVENT_CATEGORY);
        event1.setContent(EVENT_CONTENT);
        event1.setName(EVENT_NAME);
        event1.setDuration(EVENT_DURATION);

        eventRepository.save(event1);

        Address address = ADDRESS_ENTITY;
        address = addressRepository.save(address);

        Location location = new Location();
        location.setName("Filip's Cool Location");
        location.setAddress(address);
        locationRepository.save(location);

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        seatingPlanLayout.setSeatingLayoutPath(SEATINGPLANLAYOUT_PATH);
        seatingPlanLayoutRepository.save(seatingPlanLayout);

        SeatingPlan seatingPlan = new SeatingPlan();
        seatingPlan.setName("Filip's Corresponding Seating Plan");
        seatingPlan.setLocation(location);
        seatingPlan.setSeatingPlanLayout(seatingPlanLayout);
        seatingPlan = seatingPlanRepository.save(seatingPlan);

        Sector sector1 = new Sector(SECTOR_ID1);
        sector1.setSeatingPlan(seatingPlan);
        Sector sector2 = new Sector(SECTOR_ID2);
        sector2.setSeatingPlan(seatingPlan);
        sector1 = sectorRepository.save(sector1);
        sector2 = sectorRepository.save(sector2);

        Seat seat1 = new Seat();
        seat1.setSeatId(SEAT_ID1);
        seat1.setSector(sector1);
        Seat seat2 = new Seat();
        seat2.setSeatId(SEAT_ID2);
        seat2.setSector(sector2);
        Seat seat3 = new Seat();
        seat3.setSeatId(SEAT_ID3);
        seat3.setSector(sector2);
        seat1 = seatRepository.save(seat1);
        seat2 = seatRepository.save(seat2);
        seat3 = seatRepository.save(seat3);

        ShowWithoutIdDto showDtoToSave = new ShowWithoutIdDto();
        showDtoToSave.setDate(SHOW_DATE);
        showDtoToSave.setEvent(event1.getEventId().intValue());
        showDtoToSave.setSeatingPlan(Math.toIntExact(seatingPlan.getSeatingPlanId()));
        List<SectorPriceDto> sectorPriceDtos = new ArrayList<>();
        SectorPriceDto sectorPriceDto1 = new SectorPriceDto();
        sectorPriceDto1.setPrice(100F);
        sectorPriceDto1.setSectorId(sector1.getSectorId());
        sectorPriceDtos.add(sectorPriceDto1);
        SectorPriceDto sectorPriceDto2 = new SectorPriceDto();
        sectorPriceDto2.setPrice(200F);
        sectorPriceDto2.setSectorId(sector2.getSectorId());
        sectorPriceDtos.add(sectorPriceDto2);
        showDtoToSave.setSectorPrices(sectorPriceDtos);

        String json = objectMapper.writeValueAsString(showDtoToSave);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
            .post("/shows")
            .header(
                securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

        assertThat(showRepository.findAll()).hasSize(1);
        assertThat(showRepository.findAll().get(0).getDate()).isEqualTo(SHOW_DATE);

        assertThat(sectorPriceRepository.findAll()).hasSize(2);
        assertThat(sectorPriceRepository.findAll().get(0).getPrice()).isEqualTo("100.00");

        assertThat(ticketRepository.findAll()).hasSize(3);
        assertThat(ticketRepository.findAll().get(0).getSeat().getSeatId()).isEqualTo(seat1.getSeatId());
        assertThat(ticketRepository.findAll().get(1).getSeat().getSeatId()).isEqualTo(seat2.getSeatId());
        assertThat(ticketRepository.findAll().get(2).getSeat().getSeatId()).isEqualTo(seat3.getSeatId());
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
