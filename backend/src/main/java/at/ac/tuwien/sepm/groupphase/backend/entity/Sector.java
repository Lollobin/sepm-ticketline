package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectorId;

    @ManyToOne
    @JoinColumn(name = "seatingPlanId", referencedColumnName = "seatingPlanId", nullable = false)
    private SeatingPlan seatingPlan;

    @OneToMany(mappedBy = "sector")
    private List<SectorPrice> sectorPrices;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sector sector = (Sector) o;
        return sectorId == sector.sectorId && Objects.equals(seatingPlan, sector.seatingPlan);
    }

    @Override
    public String toString() {
        return "Sector{" + "sectorId=" + sectorId + ", seatingPlanId=" + seatingPlan + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectorId, seatingPlan);
    }


    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public SeatingPlan getSeatingPlan() {
        return seatingPlan;
    }

    public void setSeatingPlan(SeatingPlan seatingPlan) {
        this.seatingPlan = seatingPlan;
    }

    public List<SectorPrice> getSectorPrices() {
        return sectorPrices;
    }

    public void setSectorPrices(List<SectorPrice> sectorPrices) {
        this.sectorPrices = sectorPrices;
    }
}
