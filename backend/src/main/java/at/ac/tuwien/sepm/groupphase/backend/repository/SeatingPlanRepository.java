package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatingPlanRepository extends JpaRepository<SeatingPlan, Long> {

    Optional<List<SeatingPlan>> findAllByLocation(Location location);

    SeatingPlan getBySeatingPlanId(Long seatinPlanId);
}
