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

    Show getByShowId(long showId);

    /**
     * Searches for shows according to the parameters. The search is case-insensitive. The params
     * are connected with the AND operator.
     *
     * @param dateTime    check if the date (date + hours + minutes) is equal to the ones stored
     * @param hour        hours of the date to check for
     * @param minutes     minutes of the date to check for
     * @param eventName   name of the event to search for if the stored entries contain it
     * @param price       searches for shows with a price <= stored price
     * @param seatingPlan id of the seating plan to search for
     * @param pageable    contains information about the page
     * @return page of shows
     */
    @Query(
        "select distinct s from Show s join SectorPrice sp on sp.show.showId = s.showId join Sector"
            + " sec on sec.sectorId = sp.sector.sectorId join SeatingPlan seatP on seatP.seatingPlanId = sec.seatingPlan.seatingPlanId left join Location l on l.locationId = seatP.location.locationId "
            + " where "
            + "((:name is null) or (upper(s.event.name) like upper(concat('%', :name, '%')))) and "
            + "((:date is null) or "
            + "(:hour <> 0 and :minute <> 0 and (function('HOUR', s.date) = :hour) and function('MINUTE', s.date) = :minute and "
            + "year(s.date) = year(:date) and month(s.date) = month (:date) and day(s.date) = day(:date))) and"
            + "((:date is null) or (:hour = 0 and :minute = 0 and year(s.date) = year(:date) and month(s.date) = month (:date) and "
            + "day(s.date) = day(:date))) and" + "((:price is null) or (sp.price <= :price)) and "
            + "((:seating is null) or (seatP.seatingPlanId = :seating)) and "
            + "((:location is null) or (l.locationId = :location))")
    Page<Show> search(@Param("date") OffsetDateTime dateTime, @Param("hour") Integer hour,
        @Param("minute") Integer minutes, @Param("name") String eventName,
        @Param("price") BigDecimal price, @Param("seating") Long seatingPlan, @Param("location") Long location, Pageable pageable);

    @Query(
        "select distinct s from Show s join SectorPrice sp on sp.show.showId = s.showId join Sector"
            + " sec on sec.sectorId = sp.sector.sectorId join SeatingPlan seatP on seatP.seatingPlanId = sec.seatingPlan.seatingPlanId left join Location l on l.locationId = seatP.location.locationId "
            + "where :location = l.locationId")
    Page<Show> findShowByLocation(@Param("location") Long location, Pageable pageable);

    @Query("select s from Show s where s.event.eventId = :eventId")
    Page<Show> findShowByEventId(@Param("eventId") Long eventId, Pageable pageable);


}
