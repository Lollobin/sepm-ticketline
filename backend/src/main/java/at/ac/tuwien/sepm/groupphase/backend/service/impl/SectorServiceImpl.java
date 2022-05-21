package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SectorServiceImpl implements SectorService {

    private final SectorRepository sectorRepository;
    private final SeatingPlanRepository seatingPlanRepository;

    public SectorServiceImpl(SectorRepository sectorRepository, SeatingPlanRepository seatingPlanRepository) {
        this.sectorRepository = sectorRepository;
        this.seatingPlanRepository = seatingPlanRepository;
    }

    @Override
    public List<Sector> findAllBySeatingPlan(Long seatingPlanId) {

        Optional<SeatingPlan> seatingPlan = seatingPlanRepository.findById(seatingPlanId);
        if (seatingPlan.isEmpty()) {
            throw new NotFoundException(String.format("Could not find seatingPlan with id %s", seatingPlanId));
        }

        Optional<List<Sector>> sectors = sectorRepository.findAllBySeatingPlan(seatingPlan.get());
        if (sectors.isEmpty()) {
            throw new ConflictException("There are no sectors for the given seatingPlan");
        }

        return sectors.get();

    }
}
