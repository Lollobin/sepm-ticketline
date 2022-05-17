package at.ac.tuwien.sepm.groupphase.backend.entity.embeddables;

import java.io.Serializable;
import java.util.Objects;

public class BookedInKey implements Serializable {

    private Long transactionId;
    private Long ticketId;

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
        return "BookedInKey{" + "transactionId=" + transactionId + ", ticketId=" + ticketId + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, ticketId);
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }
}
