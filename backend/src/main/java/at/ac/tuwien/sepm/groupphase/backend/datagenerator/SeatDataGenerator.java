package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanLayoutDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSectorDto;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class SeatDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    private final SeatingPlanRepository seatingPlanRepository;
    private final LocationRepository locationRepository;
    private final FileSystemRepository fileSystemRepository;
    private final SectorRepository sectorRepository;
    private final SeatRepository seatRepository;
    private final Faker faker = new Faker();
    private static final String seatingPlanPath = "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlans/";
    private static final File[] files = new File(seatingPlanPath).listFiles();

    public SeatDataGenerator(FileSystemRepository fileSystemRepository,
        SeatingPlanLayoutRepository seatingPlanLayoutRepository,
        SeatingPlanRepository seatingPlanRepository, LocationRepository locationRepository,
        SectorRepository sectorRepository, SeatRepository seatRepository) {
        this.fileSystemRepository = fileSystemRepository;
        this.seatingPlanLayoutRepository = seatingPlanLayoutRepository;
        this.seatingPlanRepository = seatingPlanRepository;
        this.locationRepository = locationRepository;
        this.sectorRepository = sectorRepository;
        this.seatRepository = seatRepository;
    }

    private SeatingPlanLayout saveSeatingPlan(byte[] byteArray) throws IOException {

        String path = this.fileSystemRepository.save(byteArray, "test");

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        seatingPlanLayout.setSeatingLayoutPath(path);
        return seatingPlanLayoutRepository.save(seatingPlanLayout);
    }

    private SeatingPlanLayoutDto parseSeatingPlan(File file) throws IOException {

        return new ObjectMapper().readValue(FileUtil.readAsByteArray(file),
            SeatingPlanLayoutDto.class);
    }

    public void generateData(int maxSeatingPlansPerLocation) throws IOException {
        if (!seatingPlanLayoutRepository.findAll().isEmpty()) {
            LOGGER.debug("seating plan layouts already generated");
            return;
        }

        LOGGER.debug("generating seating plan layouts, seating plans, sectors and seats");
        List<Location> locations = locationRepository.findAll();
        for (Location location : locations) {
            for (int i = 0; i < faker.number().numberBetween(1, maxSeatingPlansPerLocation); i++) {
                generateSeatingPlan(location);
            }
        }

    }


    private void generateSeatingPlan(Location location) throws IOException {
        File file = files[faker.number().numberBetween(0, files.length)];
        SeatingPlanLayout seatingPlanLayout = saveSeatingPlan(FileUtil.readAsByteArray(file));
        SeatingPlanLayoutDto seatingPlanLayoutDto = parseSeatingPlan(file);
        SeatingPlan seatingPlan = generateSeatingPlan(faker.starTrek().location(), location,
            seatingPlanLayout);
        seatingPlanRepository.save(seatingPlan);
        List<Sector> sectorList = new ArrayList<>();

        for (SeatingPlanSectorDto sectorDto : seatingPlanLayoutDto.getSectors()) {
            sectorList.add(generateSector(seatingPlan));
        }
        List<Sector> updatedSectors = sectorRepository.saveAll(sectorList);
        Map<Long, SeatingPlanSectorDto> seatingPlanSectors = new HashMap<>();
        Map<Long, Sector> sectors = new HashMap<>();
        int counter = 0;
        for (SeatingPlanSectorDto sectorDto : seatingPlanLayoutDto.getSectors()) {
            seatingPlanSectors.put(sectorDto.getId(), sectorDto);
            sectors.put(sectorDto.getId(), updatedSectors.get(counter));
            sectorDto.setId(updatedSectors.get(counter).getSectorId());

            counter++;
        }

        List<Seat> seats = new ArrayList<>();
        long count = 0;
        for (SeatingPlanSeatDto seatDto : seatingPlanLayoutDto.getSeats()) {
            if (seatingPlanSectors.get(seatDto.getSectorId()).getNoSeats()) {
                seats.add(generateSeat(sectors.get(seatDto.getSectorId()), null, null));

            } else {
                count++;
                seats.add(generateSeat(sectors.get(seatDto.getSectorId()),
                    (long) Math.floor(((double) count) / 10) + 1, count % 10));
            }
        }
        List<Seat> savedSeats = seatRepository.saveAll(seats);
        counter = 0;
        for (SeatingPlanSeatDto seatDto : seatingPlanLayoutDto.getSeats()) {
            seatDto.setId(savedSeats.get(counter).getSeatId());
            seatDto.setSectorId(sectors.get(seatDto.getSectorId()).getSectorId());
            counter++;
        }
        String path = this.fileSystemRepository.save(
            new ObjectMapper().writeValueAsBytes(seatingPlanLayoutDto), "test");
        seatingPlanLayout.setSeatingLayoutPath(path);
        seatingPlanLayoutRepository.save(seatingPlanLayout);
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

    private Seat generateSeat(Sector sector, Long rowNumber, Long seatNumber) {
        Seat seat = new Seat();
        seat.setSector(sector);
        seat.setSeatNumber(rowNumber);
        seat.setRowNumber(seatNumber);

        return seat;
    }
}
