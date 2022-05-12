package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.SectorPriceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorPriceRepository extends JpaRepository<SectorPrice, SectorPriceId> {

    SectorPrice getSectorPriceByShowIdAndSectorId(@Param("showId") Long showId, @Param("sectorId") Long sectorId);

}
