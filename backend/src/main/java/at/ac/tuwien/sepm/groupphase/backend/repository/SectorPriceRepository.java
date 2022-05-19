package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.SectorPriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorPriceRepository extends JpaRepository<SectorPrice, SectorPriceId> {
    /**
     * Finds price of a sector for a given show.
     *
     * @return ordered list of al message entries
     */
    @Query(value = "SELECT * FROM sector_price s"
        + " WHERE s.show_id = (:showId)"
        + " AND  s.sector_id = (:sectorId)",
        nativeQuery = true)
    SectorPrice findOneByShowIdBySectorId(@Param("showId") Long showId, @Param("sectorId") Long sectorId);
}
