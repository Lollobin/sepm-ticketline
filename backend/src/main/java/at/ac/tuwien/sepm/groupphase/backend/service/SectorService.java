package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import java.util.List;

public interface SectorService {


    List<Sector> findAllBySeatingPlan(Long seatingPlanId);

}
