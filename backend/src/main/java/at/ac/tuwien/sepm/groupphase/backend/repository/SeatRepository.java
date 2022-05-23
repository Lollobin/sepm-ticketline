package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query(value = "SELECT * FROM seat s"
        + " NATURAL JOIN ticket t"
        + " NATURAL JOIN show s"
        + " WHERE s.show_id = (:showId)",
        nativeQuery = true)
    public List<Ticket> findByShowId();

    List<Seat> findBySectorSectorId(Long sectorId);
}
