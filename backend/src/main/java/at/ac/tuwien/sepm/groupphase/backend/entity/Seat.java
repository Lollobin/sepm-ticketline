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
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seatId;

    @Column
    private long rowNumber;

    @Column
    private long seatNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Seat seat = (Seat) o;
        return seatId == seat.seatId && rowNumber == seat.rowNumber && seatNumber == seat.seatNumber
            && Objects.equals(sector, seat.sector);
    }

    @Override
    public String toString() {
        return "Seat{" + "seatId=" + seatId + ", rowNumber=" + rowNumber + ", seatNumber="
            + seatNumber + ", sector=" + sector + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId, rowNumber, seatNumber, sector);
    }

    @ManyToOne
    @JoinColumn(name = "sectorId", referencedColumnName = "sectorId", nullable = false)
    private Sector sector;
}
