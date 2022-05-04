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
    private long sectorId;

    @Column(nullable = false)
    private float price;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sector sector = (Sector) o;
        return sectorId == sector.sectorId && Float.compare(sector.price, price) == 0
            && Objects.equals(seatingPlanId, sector.seatingPlanId);
    }

    @Override
    public String toString() {
        return "Sector{" + "sectorId=" + sectorId + ", price=" + price + ", seatingPlanId="
            + seatingPlanId + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectorId, price, seatingPlanId);
    }

    @ManyToOne
    @JoinColumn(name = "seatingPlanId", referencedColumnName = "seatingPlanId", nullable = false)
    private SeatingPlan seatingPlanId;

    public long getSectorId() {
        return sectorId;
    }

    public void setSectorId(long sectorId) {
        this.sectorId = sectorId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public SeatingPlan getSeatingPlanId() {
        return seatingPlanId;
    }

    public void setSeatingPlanId(SeatingPlan seatingPlanId) {
        this.seatingPlanId = seatingPlanId;
    }
}
