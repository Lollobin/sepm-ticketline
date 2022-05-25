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

    @Query("select distinct s from Show s join SectorPrice sp on sp.show.showId = s.showId join Sector"
        + " sec on sec.sectorId = sp.sector.sectorId join SeatingPlan seatP on seatP.seatingPlanId = sec.seatingPlan.seatingPlanId where ((:name is null) or (upper(s.event.name) like upper(concat('%', :name, '%')))) and "
        + "((:date is null) or (s.date = :date)) and "
        + "((:price is null) or (sp.price <= :price)) and "
        + "((:seating is null) or (seatP.seatingPlanId = :seating))")
    Page<Show> search(@Param("date") OffsetDateTime dateTime, @Param("name") String eventName,
        @Param("price") BigDecimal price, @Param("seating") Integer seatingPlan, Pageable pageable);

}
