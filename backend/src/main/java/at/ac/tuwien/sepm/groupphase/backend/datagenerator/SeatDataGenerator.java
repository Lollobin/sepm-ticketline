package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class SeatDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final SeatRepository seatRepository;
    private final SectorRepository sectorRepository;

    public SeatDataGenerator(
        SeatRepository seatRepository,
        SectorRepository sectorRepository) {
        this.seatRepository = seatRepository;
        this.sectorRepository = sectorRepository;
    }

    public void generateSeats() {
        if (!sectorRepository.findAll().isEmpty()) {
            LOGGER.debug("seats already generated");
            return;
        }

        LOGGER.debug("generating seats for each sector");

        List<Sector> sectors = sectorRepository.findAll();

        for (Sector sector : sectors) {
            // TODO: generate seats based on seating plan layout
            for (int i = 0; i < 5; i++) {
                Seat seat = new Seat();
                seat.setSector(sector);
                seat.setSeatNumber((long) Math.floor(Math.random() * 10));
                seat.setRowNumber((long) Math.floor(Math.random() * 10));
                seatRepository.save(seat);
            }
        }

    }
}
