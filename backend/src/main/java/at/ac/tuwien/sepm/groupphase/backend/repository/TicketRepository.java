package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "SELECT * FROM ticket t"
        + " NATURAL JOIN show s"
        + " WHERE s.show_id = (:showId)",
        nativeQuery = true)
    public List<Ticket> findByShowId(@Param("showId") Long showId);

    List<Ticket> findAllByPurchasedByEmailOrReservedByEmail(String purchasedByEmail, String reservedByEmail);
}
