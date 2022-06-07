package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ALBERTINA_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ALBERTINA_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ALBERTINA_ZIP_CODE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.BOLLWERK_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.BOLLWERK_COUNTRY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_ZIP_CODE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION1_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION2_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION3_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION4_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION5_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_ALBERTINA_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_BOLLWERK_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_GASOMETER_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_STADTHALLE_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_TOMORROWLAND_ADDRESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
class LocationEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    public void beforeEach() {
        locationRepository.deleteAll();
    }

    @Test
    void getWithCityAndName_shouldReturnLocationWithCityWienAndNameBollwerk() throws Exception {

        saveFiveLocations();

        String city = "klag";
        String name = "boll";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/locations").param("city", city).param("name", name)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        LocationSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), LocationSearchResultDto.class);

        List<LocationDto> locationList = searchResultDto.getLocations();

        assertThat(servletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertThat(locationList).hasSize(1);
        assertThat(locationList.get(0).getName()).isEqualTo(LOCATION3_NAME);
        assertThat(locationList.get(0).getAddress().getCity()).isEqualTo(BOLLWERK_CITY);
        assertThat(locationList.get(0).getAddress().getCountry()).isEqualTo(BOLLWERK_COUNTRY);
    }

    @Test
    void getWithValidCityButInvalidName_shouldReturnLocationWithCityWien() throws Exception {

        saveFiveLocations();

        String city = "wien";
        String name = "boll";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/locations").param("city", city).param("name", name)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        LocationSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), LocationSearchResultDto.class);

        List<LocationDto> locationList = searchResultDto.getLocations();

        assertThat(servletResponse.getStatus()).isEqualTo(HttpStatus.OK.value());

        assertAll(() -> assertEquals(2, locationList.size()),
            () -> assertEquals(LOCATION5_NAME, locationList.get(0).getName()),
            () -> assertEquals(ALBERTINA_CITY, locationList.get(0).getAddress().getCity()),
            () -> assertEquals(ALBERTINA_STREET, locationList.get(0).getAddress().getStreet()),
            () -> assertEquals(ALBERTINA_ZIP_CODE, locationList.get(0).getAddress().getZipCode()),

            () -> assertEquals(LOCATION1_NAME, locationList.get(1).getName()),
            () -> assertEquals(GASOMETER_CITY, locationList.get(1).getAddress().getCity()),
            () -> assertEquals(GASOMETER_STREET, locationList.get(1).getAddress().getStreet()),
            () -> assertEquals(GASOMETER_ZIP_CODE, locationList.get(1).getAddress().getZipCode()));
    }

    @Test
    void getWithInvalidSearchName_shouldThrowValidationException() throws Exception {

        saveFiveLocations();

        //256 chars
        String invalidName = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestejfk";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/locations").param("name", invalidName)).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        assertThat(servletResponse.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void getWithStreetAndBlankZip_shouldReturnAllLocations() throws Exception {

        saveFiveLocations();

        String streetBlank = "   ";
        String zipBlank = "   ";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/locations").param("street", streetBlank)
                .param("zipCode", zipBlank)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        LocationSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), LocationSearchResultDto.class);

        List<LocationDto> locationDtos = searchResultDto.getLocations();

        assertAll(() -> assertEquals(5, locationDtos.size()),
            () -> assertEquals(LOCATION5_NAME, locationDtos.get(0).getName()),
            () -> assertEquals(LOCATION3_NAME, locationDtos.get(1).getName()),
            () -> assertEquals(LOCATION1_NAME, locationDtos.get(2).getName()),
            () -> assertEquals(LOCATION2_NAME, locationDtos.get(3).getName()),
            () -> assertEquals(LOCATION4_NAME, locationDtos.get(4).getName()));

    }

    private void saveFiveLocations() {
        LOCATION_GASOMETER_ADDRESS.setAddressId(null);

        Location location1 = new Location();
        location1.setAddress(LOCATION_GASOMETER_ADDRESS);
        location1.setName(LOCATION1_NAME);

        locationRepository.save(location1);

        LOCATION_STADTHALLE_ADDRESS.setAddressId(null);

        Location location2 = new Location();
        location2.setAddress(LOCATION_STADTHALLE_ADDRESS);
        location2.setName(LOCATION2_NAME);

        locationRepository.save(location2);

        LOCATION_BOLLWERK_ADDRESS.setAddressId(null);

        Location location3 = new Location();
        location3.setAddress(LOCATION_BOLLWERK_ADDRESS);
        location3.setName(LOCATION3_NAME);

        locationRepository.save(location3);

        LOCATION_TOMORROWLAND_ADDRESS.setAddressId(null);

        Location location4 = new Location();
        location4.setAddress(LOCATION_TOMORROWLAND_ADDRESS);
        location4.setName(LOCATION4_NAME);

        locationRepository.save(location4);

        LOCATION_ALBERTINA_ADDRESS.setAddressId(null);

        Location location5 = new Location();
        location5.setAddress(LOCATION_ALBERTINA_ADDRESS);
        location5.setName(LOCATION5_NAME);

        locationRepository.save(location5);

    }

    @Test
    void postLocation_shouldCreateLocation() throws Exception {
        AddressDto address = new AddressDto();
        address.setCountry("Test");
        address.setCity("Test");
        address.setHouseNumber("Test");
        address.setStreet("Test");
        address.setZipCode("Test");

        LocationWithoutIdDto locationToSave = new LocationWithoutIdDto();
        locationToSave.setAddress(address);
        locationToSave.setName("TestLocation");
        String json = objectMapper.writeValueAsString(locationToSave);
        assertThat(locationRepository.findAll()).isEmpty();

        ResultActions resultAction = mockMvc.perform(MockMvcRequestBuilders
            .post("/locations")
            .header(
                securityProperties.getAuthHeader(),
                jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            .content(json)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());


        assertThat(locationRepository.findAll()).hasSize(1);
    }

}
