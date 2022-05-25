package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShowRepository extends JpaRepository<Show, Long> {

    @Query(
        "select distinct s from Show s join SectorPrice sectorPrice on s.showId = sectorPrice.show.showId join Sector"
            + " sector on sector.sectorId = sectorPrice.sector.sectorId join SeatingPlan seatingPlan on seatingPlan.seatingPlanId = sector.seatingPlan.seatingPlanId "
            + "where "
            + "((:data is null) or s.date = (:date)) and "
            + "((:name is null) or upper(s.event.name) = upper(:name)) and"
            + "((:maxPrice is null) or sectorPrice.price <= (:maxPrice)) and"
            + "((:plan is null) or seatingPlan.seatingPlanId = (:plan))")
    Page<Show> search(@Param("date") OffsetDateTime date, @Param("name") String eventName,
        @Param("maxPrice") BigDecimal maxPrice, @Param("plan") Integer seatingPlan, Pageable pageable);

}
