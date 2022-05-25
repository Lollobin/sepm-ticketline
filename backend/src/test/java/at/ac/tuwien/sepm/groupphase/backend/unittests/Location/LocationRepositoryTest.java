package at.ac.tuwien.sepm.groupphase.backend.unittests.Location;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.BOLLWERK_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.BOLLWERK_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.GASOMETER_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION1_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION2_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION3_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION4_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_BOLLWERK_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_GASOMETER_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_STADTHALLE_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION_TOMORROWLAND_ADDRESS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STADTHALLE_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.STADTHALLE_STREET;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.TOMORROWLAND_CITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.TOMORROWLAND_STREET;
import static org.assertj.core.api.Assertions.assertThat;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Test
    void searchWithNameAndCity_shouldReturnGasometerWienLocation(){

        saveFourLocations();

        String city = "wie";
        String name = "gaso";

        List<Location> locationPage = locationRepository.search(name, city, null, null, null,
            PageRequest.of(0,10, Sort.by("id").ascending())).getContent();

        assertThat(locationPage).hasSize(1);
        assertThat(locationPage.get(0).getName()).isEqualTo(LOCATION1_NAME);
        assertThat(locationPage.get(0).getAddress().getCity()).isEqualTo(GASOMETER_CITY);

        locationRepository.deleteAll();
        addressRepository.deleteAll();

    }

    @Test
    void searchWithIncorrectNameAndCorrectCity_shouldReturnGrazLocation(){

        saveFourLocations();

        String city = "graz";
        String name = "gaso";

        List<Location> locationPage = locationRepository.search(name, city, null, null, null,
            PageRequest.of(0,10, Sort.by("id").ascending())).getContent();

        assertThat(locationPage).hasSize(1);
        assertThat(locationPage.get(0).getName()).isEqualTo(LOCATION2_NAME);
        assertThat(locationPage.get(0).getAddress().getCity()).isEqualTo(STADTHALLE_CITY);

        locationRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void searchWithAllFieldsStartWithCorrectNames_shouldReturnTomorrowlandEntity(){

        saveFourLocations();

        String city = "bo";
        String name = "tomorrow";
        String country = "belg";
        String zipCode = "2850";
        String street = "Schommelei";

        List<Location> locationPage = locationRepository.search(name, city, country, street, zipCode,
            PageRequest.of(0,10, Sort.by("id").ascending())).getContent();

        assertThat(locationPage).hasSize(1);
        assertThat(locationPage.get(0).getName()).isEqualTo(LOCATION4_NAME);
        assertThat(locationPage.get(0).getAddress().getCity()).isEqualTo(TOMORROWLAND_CITY);
        assertThat(locationPage.get(0).getAddress().getStreet()).isEqualTo(TOMORROWLAND_STREET);

        locationRepository.deleteAll();
        addressRepository.deleteAll();
    }

    @Test
    void searchWithCountryStartingWithAust_shouldReturnLocationBollWerkAndGasometerAndStadthalle(){

        saveFourLocations();

        String country = "Aust";

        List<Location> locationPage = locationRepository.search(null, null, country,null, null,
            PageRequest.of(0,10, Sort.by("id").ascending())).getContent();

        assertThat(locationPage).hasSize(3);
        assertThat(locationPage.get(0).getName()).isEqualTo(LOCATION1_NAME);
        assertThat(locationPage.get(0).getAddress().getCity()).isEqualTo(GASOMETER_CITY);
        assertThat(locationPage.get(0).getAddress().getStreet()).isEqualTo(GASOMETER_STREET);

        assertThat(locationPage.get(1).getName()).isEqualTo(LOCATION2_NAME);
        assertThat(locationPage.get(1).getAddress().getCity()).isEqualTo(STADTHALLE_CITY);
        assertThat(locationPage.get(1).getAddress().getStreet()).isEqualTo(STADTHALLE_STREET);

        assertThat(locationPage.get(2).getName()).isEqualTo(LOCATION3_NAME);
        assertThat(locationPage.get(2).getAddress().getCity()).isEqualTo(BOLLWERK_CITY);
        assertThat(locationPage.get(2).getAddress().getStreet()).isEqualTo(BOLLWERK_STREET);

        locationRepository.deleteAll();
        addressRepository.deleteAll();
    }



    private void saveFourLocations(){
        LOCATION_GASOMETER_ADDRESS.setAddressId(null);
        addressRepository.save(LOCATION_GASOMETER_ADDRESS);

        Location location1 = new Location();
        location1.setAddress(LOCATION_GASOMETER_ADDRESS);
        location1.setName(LOCATION1_NAME);

        locationRepository.save(location1);

        LOCATION_STADTHALLE_ADDRESS.setAddressId(null);
        addressRepository.save(LOCATION_STADTHALLE_ADDRESS);

        Location location2 = new Location();
        location2.setAddress(LOCATION_STADTHALLE_ADDRESS);
        location2.setName(LOCATION2_NAME);

        locationRepository.save(location2);

        LOCATION_BOLLWERK_ADDRESS.setAddressId(null);
        addressRepository.save(LOCATION_BOLLWERK_ADDRESS);

        Location location3 = new Location();
        location3.setAddress(LOCATION_BOLLWERK_ADDRESS);
        location3.setName(LOCATION3_NAME);

        locationRepository.save(location3);

        LOCATION_TOMORROWLAND_ADDRESS.setAddressId(null);
        addressRepository.save(LOCATION_TOMORROWLAND_ADDRESS);

        Location location4 = new Location();
        location4.setAddress(LOCATION_TOMORROWLAND_ADDRESS);
        location4.setName(LOCATION4_NAME);

        locationRepository.save(location4);

    }
}
