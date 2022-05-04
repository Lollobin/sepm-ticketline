package at.ac.tuwien.sepm.groupphase.backend.entity.embeddables;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SectorPriceId implements Serializable {
    private Long sectorId;
    private Long showId;
}
