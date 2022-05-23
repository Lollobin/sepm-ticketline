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
//    @Query("""
//        select distinct Event.eventId, Event.name, Event.category, Event.content, Event.duration from Event join Show on Event.eventId = Show.event.eventId
//            join PlaysIn on Show.showId = PlaysIn.show.showId join Artist on PlaysIn.artist.artistId = Artist.artistId
//            join SectorPrice on Show.showId = SectorPrice.show.showId join Sector sector on SectorPrice.sector.sectorId = sector.sectorId
//            join SeatingPlan on sector.seatingPlan.seatingPlanId = SeatingPlan.seatingPlanId
//            join Location on SeatingPlan.location.locationId = Location.locationId
//        where ((:name is null) or (upper(Event.name) like upper(concat(:name, '%'))))
//        and ((:duration is null) or (Event.duration between :duration - 15 and :duration + 15))
//        and ((:content is null) or (upper(Event.content) like upper(concat('%', :content, '%'))))
//        and ((:category is null) or (upper(Event.category) like upper(:category)))
//        and ((:location is null) or (Location.locationId = :location))
//        and ((:artist is null) or (Artist.artistId = :artist))
//        """)
//    @Query("""
//            select distinct event1.eventId, event1.name, event1.category, event1.content, event1.duration from Event event1
//                join Show show1 on event1.eventId = show1.event.eventId
//                join PlaysIn playsIn1 on show1.showId = playsIn1.show.showId join Artist artist1 on playsIn1.artist.artistId = artist1.artistId
//                join SectorPrice sectorPrice1 on show1.showId = sectorPrice1.show.showId join Sector sector1 on sectorPrice1.sector.sectorId = sector1.sectorId
//                join SeatingPlan seatingPlan1 on sector1.seatingPlan.seatingPlanId = seatingPlan1.seatingPlanId
//                join Location location1 on seatingPlan1.location.locationId = location1.locationId
//            where (:name is null) or upper(event1.name) like concat(upper(:name), '%')
//            and (:duration is null) or (event1.duration between :duration - 15 and :duration + 15)
//            and (:content is null) or upper(event1.content) like upper(concat('%', :content, '%'))
//            and (:category is null) or upper(event1.category) like upper(:category)
//            and (:location is null) or (location1.locationId = :location)
//            and (:artist is null) or (artist1.artistId = :artist)
//            """)
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
            and ((:name is null) or upper(event1.name) like concat(upper(:name), '%'))
            and ((:duration is null) or (event1.duration between :duration - 15 and :duration + 15))
            and ((:content is null) or upper(event1.content) like upper(concat('%', :content, '%')))
            and ((:category is null) or upper(event1.category) like upper(:category))
            """)
    Page<Event> search(
        @Param(value = "name") String name, @Param(value = "content") String content, @Param(value = "duration") Integer duration,
        @Param(value = "category") String category, @Param(value = "location") Integer location, @Param(value = "artist") Integer artist,
        Pageable pageable);

}
