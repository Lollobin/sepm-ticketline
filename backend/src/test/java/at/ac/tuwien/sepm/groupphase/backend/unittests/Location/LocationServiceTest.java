package at.ac.tuwien.sepm.groupphase.backend.unittests.Location;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ALBERTINA_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ALBERTINA_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.BOLLWERK_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.BOLLWERK_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_COUNTRY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_STREET;
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
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STADTHALLE_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STADTHALLE_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.TOMORROWLAND_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.TOMORROWLAND_STREET;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatingPlanMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.LocationServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.SearchValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LocationServiceTest {

    Pageable pageable = PageRequest.of(0, 10, Direction.fromString("ASC"), "id");
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private SeatingPlanRepository seatingPlanRepository;
    @Spy
    private LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);
    @Spy
    private SeatingPlanMapper seatingPlanMapper = Mappers.getMapper(SeatingPlanMapper.class);
    @Mock
    private SearchValidator searchValidator;
    private LocationService locationService;

    @BeforeEach
    void setUp() {
        locationService = new LocationServiceImpl(locationRepository, locationMapper,
            searchValidator, seatingPlanRepository, seatingPlanMapper);
        locationRepository.deleteAll();
    }

    @Test
    void findAllWithStandardPageable_shouldReturnAllLocations() {

        Page<Location> locationPage = saveFiveLocations();

        when(locationRepository.findAll(pageable)).thenReturn(locationPage);

        LocationSearchResultDto searchResultDto = locationService.findAll(pageable);

        assertAll(() -> assertEquals(5, searchResultDto.getLocations().size()),
            () -> assertEquals(LOCATION1_NAME, searchResultDto.getLocations().get(0).getName()),
            () -> assertEquals(GASOMETER_CITY,
                searchResultDto.getLocations().get(0).getAddress().getCity()),
            () -> assertEquals(GASOMETER_STREET,
                searchResultDto.getLocations().get(0).getAddress().getStreet()),

            () -> assertEquals(LOCATION2_NAME, searchResultDto.getLocations().get(1).getName()),
            () -> assertEquals(STADTHALLE_CITY,
                searchResultDto.getLocations().get(1).getAddress().getCity()),
            () -> assertEquals(STADTHALLE_STREET,
                searchResultDto.getLocations().get(1).getAddress().getStreet()),

            () -> assertEquals(LOCATION3_NAME, searchResultDto.getLocations().get(2).getName()),
            () -> assertEquals(BOLLWERK_CITY,
                searchResultDto.getLocations().get(2).getAddress().getCity()),
            () -> assertEquals(BOLLWERK_STREET,
                searchResultDto.getLocations().get(2).getAddress().getStreet()),

            () -> assertEquals(LOCATION4_NAME, searchResultDto.getLocations().get(3).getName()),
            () -> assertEquals(TOMORROWLAND_CITY,
                searchResultDto.getLocations().get(3).getAddress().getCity()),
            () -> assertEquals(TOMORROWLAND_STREET,
                searchResultDto.getLocations().get(3).getAddress().getStreet()),

            () -> assertEquals(LOCATION5_NAME, searchResultDto.getLocations().get(4).getName()),
            () -> assertEquals(ALBERTINA_CITY,
                searchResultDto.getLocations().get(4).getAddress().getCity()),
            () -> assertEquals(ALBERTINA_STREET,
                searchResultDto.getLocations().get(4).getAddress().getStreet()));
    }

    @Test
    void search_shouldReturnAllCorrectLocations() {

        //testing name and city

        Location location1 = new Location();
        location1.setAddress(LOCATION_GASOMETER_ADDRESS);
        location1.setName(LOCATION1_NAME);

        Location location2 = new Location();
        location2.setAddress(LOCATION_STADTHALLE_ADDRESS);
        location2.setName(LOCATION2_NAME);

        Location location3 = new Location();
        location3.setAddress(LOCATION_BOLLWERK_ADDRESS);
        location3.setName(LOCATION3_NAME);

        String name = "gaso";
        String city = "wien";

        LocationSearchDto nameAndCitySearchDto = new LocationSearchDto();
        nameAndCitySearchDto.setCity(city);
        nameAndCitySearchDto.setName(name);

        List<Location> nameAndCityList = new ArrayList<>();
        nameAndCityList.add(location1);

        Page<Location> nameAndCityPage = new PageImpl<>(nameAndCityList);

        when(locationRepository.search(name, city, null, null, null, pageable)).thenReturn(
            nameAndCityPage);

        LocationSearchResultDto resultDtoWithNameAndCity = locationService.search(
            nameAndCitySearchDto, pageable);

        assertAll(() -> assertEquals(1, resultDtoWithNameAndCity.getLocations().size()),
            () -> assertEquals(GASOMETER_CITY,
                resultDtoWithNameAndCity.getLocations().get(0).getAddress().getCity()),
            () -> assertEquals(GASOMETER_COUNTRY,
                resultDtoWithNameAndCity.getLocations().get(0).getAddress().getCountry()));

        //testing country

        String country = "austria";

        LocationSearchDto countrySearchDto = new LocationSearchDto();
        countrySearchDto.setCountry(country);

        List<Location> countryList = new ArrayList<>();
        countryList.add(location1);
        countryList.add(location2);
        countryList.add(location3);

        Page<Location> countryPage = new PageImpl<>(countryList);

        when(locationRepository.search(null, null, country, null, null, pageable)).thenReturn(
            countryPage);

        LocationSearchResultDto resultDtoWithCountry = locationService.search(countrySearchDto,
            pageable);

        assertAll(() -> assertEquals(3, resultDtoWithCountry.getLocations().size()),
            () -> assertEquals(GASOMETER_CITY,
                resultDtoWithCountry.getLocations().get(0).getAddress().getCity()),
            () -> assertEquals(STADTHALLE_CITY,
                resultDtoWithCountry.getLocations().get(1).getAddress().getCity()),
            () -> assertEquals(BOLLWERK_CITY,
                resultDtoWithCountry.getLocations().get(2).getAddress().getCity()));

        //testing with no result

        String invalid = "jfklöhjeagklerwagklöre";

        LocationSearchDto invalidSearchDto = new LocationSearchDto();
        invalidSearchDto.setCountry(invalid);

        Page<Location> invalidPage = new PageImpl<>(new ArrayList<>());

        when(locationRepository.search(null, null, invalid, null, null, pageable)).thenReturn(
            invalidPage);

        LocationSearchResultDto resultDtoInvalidSearch = locationService.search(invalidSearchDto,
            pageable);

        assertThat(resultDtoInvalidSearch.getLocations()).isEmpty();


    }

    private Page<Location> saveFiveLocations() {

        Location location1 = new Location();
        location1.setAddress(LOCATION_GASOMETER_ADDRESS);
        location1.setName(LOCATION1_NAME);

        Location location2 = new Location();
        location2.setAddress(LOCATION_STADTHALLE_ADDRESS);
        location2.setName(LOCATION2_NAME);

        Location location3 = new Location();
        location3.setAddress(LOCATION_BOLLWERK_ADDRESS);
        location3.setName(LOCATION3_NAME);

        Location location4 = new Location();
        location4.setAddress(LOCATION_TOMORROWLAND_ADDRESS);
        location4.setName(LOCATION4_NAME);

        Location location5 = new Location();
        location5.setAddress(LOCATION_ALBERTINA_ADDRESS);
        location5.setName(LOCATION5_NAME);

        List<Location> locationList = new ArrayList<>();
        locationList.add(location1);
        locationList.add(location2);
        locationList.add(location3);
        locationList.add(location4);
        locationList.add(location5);

        return new PageImpl<>(locationList);

    }

}
