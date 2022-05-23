package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    //    @Query("""
    //        select distinct e from Event e join Show s join Artist a join SectorPrice spc join Sector sc join SeatingPlan sp join Location l
    //        where upper(e.name) like concat(upper(:name), '%')
    //        and ((:duration is null) or (e.duration between :duration - 15 and :duration + 15))
    //        and upper(e.content) like upper(concat('%', :content, '%'))
    //        and upper(e.category) like upper(:category)
    //        and ((:location is null) or (l.locationId = :location))
    //        and ((:artist is null) or (a.artistId = :artist))
    //        """)
    //    @Query("""
    //        select distinct event.eventId from Event event join Show show join Artist artist join SectorPrice sectorprice join Sector sector join SeatingPlan seatingplan join Location location
    //        where (:name is null) or upper(event.name) like concat(upper(:name), '%')
    //        and (:duration is null) or (event.duration between :duration - 15 and :duration + 15)
    //        and (:content is null) or upper(event.content) like upper(concat('%', :content, '%'))
    //        and (:category is null) or upper(event.category) like upper(:category)
    //        and (:location is null) or (location.locationId = :location)
    //        and (:artist is null) or (artist.artistId = :artist)
    //        """)
    @Query("""
        select distinct event.eventId, event.name, event.category, event.content, event.duration from Event event join Show show on event.eventId = show.event.eventId
            join PlaysIn playsIn on show.showId = playsIn.show.showId join Artist artist on playsIn.artist.artistId = artist.artistId
            join SectorPrice sectorprice on show.showId = sectorprice.show.showId join Sector sector on sectorprice.sector.sectorId = sector.sectorId
            join SeatingPlan seatingplan on sector.seatingPlan.seatingPlanId = seatingplan.seatingPlanId
            join Location location on seatingplan.location.locationId = location.locationId
        where ((:name is null) or (upper(event.name) like upper(concat(:name, '%'))))
        and ((:duration is null) or (event.duration between :duration - 15 and :duration + 15))
        and ((:content is null) or (upper(event.content) like upper(concat('%', :content, '%'))))
        and ((:category is null) or (upper(event.category) like upper(:category)))
        and ((:location is null) or (location.locationId = :location))
        and ((:artist is null) or (artist.artistId = :artist))
        """)
    Page<Event> search(
        @Param(value = "name") String name, @Param(value = "content") String content, @Param(value = "duration") Long duration,
        @Param(value = "category") String category, @Param(value = "location") Integer location, @Param(value = "artist") Integer artist,
        Pageable pageable);

}
