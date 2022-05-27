package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Finds all tickets by a show ID.
     *
     * @param showId The id of the show to find
     * @return The list of tickets in a show
     */
    @Query(value = "SELECT * FROM ticket t" + " NATURAL JOIN show s"
        + " WHERE s.show_id = (:showId)", nativeQuery = true)
    public List<Ticket> findByShowId(@Param("showId") Long showId);

    /**
     * Returns all tickets that are currently reserved or purchased by the user with the given
     * email. Only returns tickets for shows after the given date.
     *
     * <p>purchasedByEmail and reservedByEmail should be the same
     *
     * <p>dateTime1 and dateTime2 should be the same
     *
     * @param purchasedByEmail email of the user of which to return the tickets
     * @param dateTime1        usually set to today/yesterday
     * @param reservedByEmail  same as purchasedByEmail
     * @param dateTime2        same as dateTime1
     * @return tickets by user
     */
    List<Ticket> findAllByPurchasedByEmailAndShowDateAfterOrReservedByEmailAndShowDateAfterOrderByShowDateAsc(
        String purchasedByEmail, OffsetDateTime dateTime1, String reservedByEmail,
        OffsetDateTime dateTime2);
}
