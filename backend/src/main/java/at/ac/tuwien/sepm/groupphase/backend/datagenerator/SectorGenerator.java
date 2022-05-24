package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import java.lang.invoke.MethodHandles;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class SectorGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final SectorRepository sectorRepository;
    private final SeatingPlanRepository seatingPlanRepository;

    public SectorGenerator(SectorRepository sectorRepository,
        SeatingPlanRepository seatingPlanRepository) {
        this.sectorRepository = sectorRepository;
        this.seatingPlanRepository = seatingPlanRepository;
    }

    public void generateSectors() {
        if (!sectorRepository.findAll().isEmpty()) {
            LOGGER.debug("shows already generated");
            return;
        }

        LOGGER.debug("generating sectors for each seating plan");
        List<SeatingPlan> seatingPlans = seatingPlanRepository.findAll();
        for (SeatingPlan seatingPlan : seatingPlans) {
            // TODO: generate sectors based on seatingPlanLayout
            for (int i = 0; i < 5; i++) {
                Sector sector = new Sector();
                sector.setSeatingPlan(seatingPlan);
                sectorRepository.save(sector);
            }
        }
    }
}
