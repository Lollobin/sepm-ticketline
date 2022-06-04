package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Searched the table Event if there is an event that has the following parameters as fields or relations.
     * All parameters are case insensitive.
     *
     * @param name     of the searched event
     * @param content  of the searched event
     * @param duration of the searched event (+/- 30 tolerance)
     * @param category of the searched event (has to fit perfectly)
     * @param location of the searched event, if the event has show(s)
     * @param artist   of the searched event, if the event has show(s)
     * @param pageable contains information about the page we request
     * @return Page of found events
     */
    @Query("""
        select distinct event1 from Event event1
        where ((:location is null) or event1.eventId in (
            select event2.eventId from Event event2 join Show show1 on event2.eventId = show1.event.eventId
                join SectorPrice sectorPrice1 on show1.showId = sectorPrice1.show.showId join Sector sector1 on sectorPrice1.sector.sectorId = sector1.sectorId
                join SeatingPlan seatingPlan1 on sector1.seatingPlan.seatingPlanId = seatingPlan1.seatingPlanId
                join Location location1 on seatingPlan1.location.locationId = :location
        ))
        and ((:artist is null) or event1.eventId in (
            select event3.eventId from Event event3 join Show show2 on event3.eventId = show2.event.eventId
                join PlaysIn playsIn1 on show2.showId = playsIn1.show.showId join Artist artist1 on playsIn1.artist.artistId = :artist
        ))
        and ((:name is null) or upper(event1.name) like concat('%', upper(:name), '%'))
        and ((:duration is null) or (event1.duration between :duration - 30 and :duration + 30))
        and ((:content is null) or upper(event1.content) like upper(concat('%', :content, '%')))
        and ((:category is null) or upper(event1.category) like upper(:category))
        """)
    Page<Event> search(@Param(value = "name") String name, @Param(value = "content") String content, @Param(value = "duration") Integer duration,
                       @Param(value = "category") String category, @Param(value = "location") Long location, @Param(value = "artist") Long artist,
                       Pageable pageable);

    @Query(value = "SELECT e.event_id, e.name, e.duration, COUNT(*) AS ticketsSold "
        + "FROM event e NATURAL JOIN show s NATURAL JOIN ticket t "
        + "GROUP BY e.event_id ORDER BY ticketsSoldAndReserved DESC LIMIT 10",
        nativeQuery = true)
    List<Event> getTopEvents();


}
