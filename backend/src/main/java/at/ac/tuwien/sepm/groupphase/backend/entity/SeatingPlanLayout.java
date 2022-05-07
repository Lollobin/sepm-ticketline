package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SeatingPlanLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seatingPlanLayoutId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SeatingPlanLayout that = (SeatingPlanLayout) o;
        return seatingPlanLayoutId == that.seatingPlanLayoutId
            && Objects.equals(seatingLayoutPath, that.seatingLayoutPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatingPlanLayoutId, seatingLayoutPath);
    }

    @Override
    public String toString() {
        return "SeatingPlanLayout{"
            + "seatingPlanLayoutId="
            + seatingPlanLayoutId
            + ", seatingLayoutPath='"
            + seatingLayoutPath
            + '\''
            + '}';
    }

    @Column(nullable = false)
    private String seatingLayoutPath;

    public long getSeatingPlanLayoutId() {
        return seatingPlanLayoutId;
    }

    public void setSeatingPlanLayoutId(long seatingPlanLayoutId) {
        this.seatingPlanLayoutId = seatingPlanLayoutId;
    }

    public String getSeatingLayoutPath() {
        return seatingLayoutPath;
    }

    public void setSeatingLayoutPath(String seatingLayoutPath) {
        this.seatingLayoutPath = seatingLayoutPath;
    }
}
