package at.ac.tuwien.sepm.groupphase.backend.entity.embeddables;

import java.io.Serializable;
import java.util.Objects;

public class BookedInKey implements Serializable {

    private long transactionId;
    private long ticketId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookedInKey that = (BookedInKey) o;
        return transactionId == that.transactionId && ticketId == that.ticketId;
    }

    @Override
    public String toString() {
        return "BookedInKey{" +
            "transactionId=" + transactionId +
            ", ticketId=" + ticketId +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, ticketId);
    }
}
