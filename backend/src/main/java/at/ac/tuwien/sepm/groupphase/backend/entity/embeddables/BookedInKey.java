package at.ac.tuwien.sepm.groupphase.backend.entity.embeddables;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BookedInKey implements Serializable {

    @Column(name = "transaction_id")
    Long transactionId;

    @Column(name = "ticket_id")
    Long ticketId;

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

    public BookedInKey() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

}
