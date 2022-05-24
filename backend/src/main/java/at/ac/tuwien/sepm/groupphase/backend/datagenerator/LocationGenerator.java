package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class LocationGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AddressDataGenerator addressDataGenerator;
    private final LocationRepository locationRepository;
    private final Faker faker = new Faker();

    public LocationGenerator(
        AddressDataGenerator addressDataGenerator,
        LocationRepository locationRepository) {
        this.addressDataGenerator = addressDataGenerator;
        this.locationRepository = locationRepository;
    }

    public void generateData(int numberOfLocations) {

        if (!locationRepository.findAll().isEmpty()) {
            LOGGER.debug("locations already generated");
            return;
        }

        LOGGER.debug("generating {} locations", numberOfLocations);

        for (int i = 0; i < numberOfLocations; i++) {
            Location location = generateLocation();
            LOGGER.trace("saving location {}", location);
            locationRepository.save(location);
        }
    }

    private Location generateLocation(){
        Location location = new Location();
        location.setName(faker.overwatch().location());
        location.setAddress(addressDataGenerator.generateRandomAddress());
        return location;
    }
}
