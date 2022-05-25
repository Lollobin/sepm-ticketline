package at.ac.tuwien.sepm.groupphase.backend.entity.embeddables;

import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectorPriceId that = (SectorPriceId) o;
        return Objects.equals(sectorId, that.sectorId) && Objects.equals(showId,
            that.showId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectorId, showId);
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public Long getShowId() {
        return showId;
    }
}
