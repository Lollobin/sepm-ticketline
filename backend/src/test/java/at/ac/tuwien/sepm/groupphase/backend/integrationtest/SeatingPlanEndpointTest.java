package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_ROLES;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.unittests.SeatingPlan.SeatingPlanServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class SeatingPlanEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SeatingPlanRepository seatingPlanRepository;
    @Autowired
    private SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    @Autowired
    private FileSystemRepository fileSystemRepository;
    @Autowired
    private SectorRepository sectorRepository;
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    Location location;
    @BeforeEach
    public void beforeEach() {
        seatRepository.deleteAll();
        sectorRepository.deleteAll();
        seatingPlanRepository.deleteAll();
        seatingPlanLayoutRepository.deleteAll();
        locationRepository.deleteAll();
        location = new Location();
        location.setId(1L);
        location.setName("Tew");
        Address address = new Address();
        address.setZipCode("");
        address.setCity("");
        address.setStreet("");
        address.setCountry("");
        address.setHouseNumber("");
        location.setAddress(address);
        location = locationRepository.save(location);
    }

    @Test
    void postSeatingPlan_shouldCreateSeatingPlan() throws Exception {
        SeatingPlanWithoutIdDto seatingPlanWithoutIdDto = SeatingPlanServiceTest.generateSeatingPlanWithoutIdDto(
            5, 5, location.getId());
        String json = objectMapper.writeValueAsString(seatingPlanWithoutIdDto);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
                .post("/seatingPlans")
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
            .andExpect(header().exists("location"));

        assertThat(seatRepository.findAll().size()).isEqualTo(25);
        assertThat(sectorRepository.findAll().size()).isEqualTo(5);
        assertThat(seatingPlanRepository.findAll().size()).isEqualTo(1);
        assertThat(seatingPlanLayoutRepository.findAll().size()).isEqualTo(1);

    }

    @Test
    void postSeatingPlan_shouldThrowNotAuthorizedAsUser() throws Exception {
        SeatingPlanWithoutIdDto seatingPlanWithoutIdDto = SeatingPlanServiceTest.generateSeatingPlanWithoutIdDto(
            5, 5,1);
        String json = objectMapper.writeValueAsString(seatingPlanWithoutIdDto);

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
            .post("/seatingPlans")
            .header(
                securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, USER_ROLES))
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isForbidden());

    }
}
