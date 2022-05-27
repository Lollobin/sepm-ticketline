package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import com.github.javafaker.Faker;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Profile("generateData")
@Component
public class SeatDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    private final SeatingPlanRepository seatingPlanRepository;
    private final LocationRepository locationRepository;
    private final FileSystemRepository fileSystemRepository;
    private final SectorRepository sectorRepository;
    private final SeatRepository seatRepository;
    private final Faker faker = new Faker();

    public SeatDataGenerator(
        FileSystemRepository fileSystemRepository,
        SeatingPlanLayoutRepository seatingPlanLayoutRepository,
        SeatingPlanRepository seatingPlanRepository,
        LocationRepository locationRepository,
        SectorRepository sectorRepository,
        SeatRepository seatRepository) {
        this.fileSystemRepository = fileSystemRepository;
        this.seatingPlanLayoutRepository = seatingPlanLayoutRepository;
        this.seatingPlanRepository = seatingPlanRepository;
        this.locationRepository = locationRepository;
        this.sectorRepository = sectorRepository;
        this.seatRepository = seatRepository;
    }

    public void generateData(int maxSeatingPlansPerLocation) throws IOException {
        if (!seatingPlanLayoutRepository.findAll().isEmpty()) {
            LOGGER.debug("seating plan layouts already generated");
            return;
        }

        LOGGER.debug("generating seating plan layouts, seating plans, sectors and seats");

        FileInputStream fis = new FileInputStream(ResourceUtils.getFile(
                "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlan1.json")
            .getAbsoluteFile());
        String path = this.fileSystemRepository.save(
            fis.readAllBytes(), "test");
        fis.close();

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        seatingPlanLayout.setSeatingLayoutPath(path);
        seatingPlanLayoutRepository.save(seatingPlanLayout);

        List<Location> locations = locationRepository.findAll();

        for (Location location : locations) {
            int numberOfSeatingPlans = faker.number().numberBetween(1, maxSeatingPlansPerLocation);

            //generate seatingplans for each location
            for (int i = 0; i < numberOfSeatingPlans; i++) {

                int numberOfSeatingPlanLayouts = seatingPlanLayoutRepository.findAll().size();
                SeatingPlanLayout randomLayout = seatingPlanLayoutRepository.getById(
                    (long) faker.number().numberBetween(1, numberOfSeatingPlanLayouts));

                SeatingPlan seatingPlan = generateSeatingPlan(String.valueOf((char) (i + 65)),
                    location, randomLayout);
                seatingPlanRepository.save(seatingPlan);

                // generate sectors for each seating plan
                // TODO: generate sectors based on seating plan layout
                for (int j = 0; j < 5; j++) {
                    Sector sector = generateSector(seatingPlan);
                    sectorRepository.save(sector);

                    // generate seats for each sector
                    // TODO: generate seats based on seating plan layout
                    for (int k = 0; k < 5; k++) {
                        Seat seat = generateSeat(sector);
                        seatRepository.save(seat);
                    }
                }
            }
        }
    }

    private SeatingPlan generateSeatingPlan(String name, Location location,
        SeatingPlanLayout seatingPlanLayout) {
        SeatingPlan seatingPlan = new SeatingPlan();
        seatingPlan.setName(name);
        seatingPlan.setLocation(location);
        seatingPlan.setSeatingPlanLayout(seatingPlanLayout);
        return seatingPlan;
    }

    private Sector generateSector(SeatingPlan seatingPlan) {
        Sector sector = new Sector();
        sector.setSeatingPlan(seatingPlan);
        return sector;
    }

    private Seat generateSeat(Sector sector) {
        Seat seat = new Seat();
        seat.setSector(sector);
        seat.setSeatNumber((long) Math.floor(Math.random() * 10));
        seat.setRowNumber((long) Math.floor(Math.random() * 10));
        return seat;
    }
}
