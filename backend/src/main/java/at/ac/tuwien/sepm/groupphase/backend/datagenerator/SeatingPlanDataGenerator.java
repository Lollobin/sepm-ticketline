package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class SeatingPlanDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final SeatingPlanRepository seatingPlanRepository;
    private final LocationRepository locationRepository;
    private final SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    private final Faker faker = new Faker();

    public SeatingPlanDataGenerator(
        SeatingPlanRepository seatingPlanRepository,
        LocationRepository locationRepository,
        SeatingPlanLayoutRepository seatingPlanLayoutRepository) {
        this.seatingPlanRepository = seatingPlanRepository;
        this.locationRepository = locationRepository;
        this.seatingPlanLayoutRepository = seatingPlanLayoutRepository;
    }

    public void generateSeatingPlans(int maxSeatingPlansPerLocation) {

        if (!seatingPlanRepository.findAll().isEmpty()) {
            LOGGER.debug("seating plans already generated");
            return;
        }

        LOGGER.debug("generating seating plans for each location");
        List<Location> locations = locationRepository.findAll();

        for (Location location : locations) {
            int numberOfSeatingPlans = faker.number().numberBetween(1, maxSeatingPlansPerLocation);

            for (int i = 0; i < numberOfSeatingPlans; i++) {
                SeatingPlan seatingPlan = new SeatingPlan();
                seatingPlan.setName(String.valueOf((char) (i + 65)));
                seatingPlan.setLocation(location);

                int numberOfSeatingPlanLayouts = seatingPlanLayoutRepository.findAll().size();
                SeatingPlanLayout randomLayout = seatingPlanLayoutRepository.getById(
                    (long) faker.number().numberBetween(1, numberOfSeatingPlanLayouts));
                seatingPlan.setSeatingPlanLayout(randomLayout);

                seatingPlanRepository.save(seatingPlan);
            }
        }

    }
}
