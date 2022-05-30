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
                "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlan2_19_seats.json")
            .getAbsoluteFile());
        String path = this.fileSystemRepository.save(
            fis.readAllBytes(), "test");
        fis.close();

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        seatingPlanLayout.setSeatingLayoutPath(path);
        seatingPlanLayoutRepository.save(seatingPlanLayout);

        List<Location> locations = locationRepository.findAll();
        SeatingPlan seatingPlan = generateSeatingPlan("Hall A",
            locations.get(0), seatingPlanLayout);
        seatingPlanRepository.save(seatingPlan);

        // TODO: generate sectors and seats based on seating plan layout
        Sector sector1 = generateSector(seatingPlan);
        Sector sector2 = generateSector(seatingPlan);
        Sector sector3 = generateSector(seatingPlan);
        sectorRepository.save(sector1);
        sectorRepository.save(sector2);
        sectorRepository.save(sector3);

        for (int j = 0; j < 5; j++) {
            seatRepository.save(generateSeat(sector1));
        }
        for (int j = 0; j < 5; j++) {
            seatRepository.save(generateSeat(sector2));
        }
        for (int j = 0; j < 9; j++) {
            seatRepository.save(generateSeat(sector3));
        }

        fis = new FileInputStream(ResourceUtils.getFile(
                "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlan3_139_seats.json")
            .getAbsoluteFile());
        path = this.fileSystemRepository.save(
            fis.readAllBytes(), "test");
        fis.close();

        seatingPlanLayout = new SeatingPlanLayout();
        seatingPlanLayout.setSeatingLayoutPath(path);
        seatingPlanLayoutRepository.save(seatingPlanLayout);

        seatingPlan = generateSeatingPlan("Hall B",
            locations.get(0), seatingPlanLayout);
        seatingPlanRepository.save(seatingPlan);

        sector1 = generateSector(seatingPlan);
        sector2 = generateSector(seatingPlan);
        sector3 = generateSector(seatingPlan);
        Sector sector4 = generateSector(seatingPlan);
        sectorRepository.save(sector1);
        sectorRepository.save(sector2);
        sectorRepository.save(sector3);
        sectorRepository.save(sector4);

        for (int j = 0; j < 50; j++) {
            seatRepository.save(generateSeat(sector1));
        }
        for (int j = 0; j < 50; j++) {
            seatRepository.save(generateSeat(sector2));
        }
        for (int j = 0; j < 20; j++) {
            seatRepository.save(generateSeat(sector3));
        }
        for (int j = 0; j < 20; j++) {
            seatRepository.save(generateSeat(sector4));
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
