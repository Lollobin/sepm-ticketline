package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sector sector = (Sector) o;
        return Objects.equals(sectorId == sector.sectorId) && Float.compare(sector.price, price) == 0
            && Objects.equals(seatingPlanId, sector.seatingPlanId);
    }

    @Override
    public String toString() {
        return "Sector{" + "sectorId=" + sectorId + ", seatingPlanId=" + seatingPlan + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectorId, seatingPlan);
    }

    @ManyToOne
    @JoinColumn(name = "seatingPlanId", referencedColumnName = "seatingPlanId", nullable = false)
    private SeatingPlan seatingPlan;

    public long getSectorId() {
        return sectorId;
    }

    public void setSectorId(long sectorId) {
        this.sectorId = sectorId;
    }

    public SeatingPlan getSeatingPlan() {
        return seatingPlan;
    }

    public void setSeatingPlan(SeatingPlan seatingPlan) {
        this.seatingPlan = seatingPlan;
    }
}
