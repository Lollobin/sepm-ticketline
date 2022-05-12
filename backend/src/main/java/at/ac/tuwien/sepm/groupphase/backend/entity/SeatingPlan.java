package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class SeatingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seatingPlanId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "locationId", referencedColumnName = "locationId", nullable = false)
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeatingPlan that = (SeatingPlan) o;
        return seatingPlanId == that.seatingPlanId
            && Objects.equals(name, that.name)
            && Objects.equals(location, that.location)
            && Objects.equals(seatingPlanLayout, that.seatingPlanLayout);
    }

    @Override
    public String toString() {
        return "SeatingPlan{"
            + "seatingPlanId="
            + seatingPlanId
            + ", name='"
            + name
            + '\''
            + ", location="
            + location
            + ", seatingPlanLayout="
            + seatingPlanLayout
            + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatingPlanId, name, location, seatingPlanLayout);
    }

    @OneToOne
    @JoinColumn(
        name = "seatingPlanLayoutId",
        referencedColumnName = "seatingPlanLayoutId",
        nullable = false)
    private SeatingPlanLayout seatingPlanLayout;

    public long getSeatingPlanId() {
        return seatingPlanId;
    }

    public void setSeatingPlanId(long seatingPlanId) {
        this.seatingPlanId = seatingPlanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public SeatingPlanLayout getSeatingPlanLayout() {
        return seatingPlanLayout;
    }

    public void setSeatingPlanLayout(SeatingPlanLayout seatingPlanLayout) {
        this.seatingPlanLayout = seatingPlanLayout;
    }
}
