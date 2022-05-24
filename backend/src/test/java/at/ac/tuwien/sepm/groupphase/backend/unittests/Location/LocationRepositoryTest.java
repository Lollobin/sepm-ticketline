package at.ac.tuwien.sepm.groupphase.backend.unittests.Location;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS2_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS3_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS4_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION1_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION2_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION3_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.LOCATION4_NAME;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    void searchWithNameAndCity_shouldReturnOneEntity(){

        saveFourLocations();

        Page<Location> locationPage;

    }

    private void saveFourLocations(){


        Location location1 = new Location();
        location1.setAddress(ADDRESS_ENTITY);
        location1.setName(LOCATION1_NAME);

        locationRepository.save(location1);

        Location location2 = new Location();
        location1.setAddress(ADDRESS2_ENTITY);
        location1.setName(LOCATION2_NAME);

        locationRepository.save(location2);

        Location location3 = new Location();
        location1.setAddress(ADDRESS3_ENTITY);
        location1.setName(LOCATION3_NAME);

        locationRepository.save(location3);

        Location location4 = new Location();
        location1.setAddress(ADDRESS4_ENTITY);
        location1.setName(LOCATION4_NAME);

        locationRepository.save(location4);

    }
}
