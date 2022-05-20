package at.ac.tuwien.sepm.groupphase.backend.entity.embeddables;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class SectorPriceId implements Serializable {
    private Long sectorId;
    private Long showId;

    public SectorPriceId() {
    }

    public SectorPriceId(Long sectorId, Long showId) {
        this.sectorId = sectorId;
        this.showId = showId;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }
}
