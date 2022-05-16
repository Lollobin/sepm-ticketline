package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.BookedInKey;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class BookedIn {

    @EmbeddedId
    BookedInKey id = new BookedInKey();

    @ManyToOne
    @MapsId("transactionId")
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @MapsId("ticketId")
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private BookingType bookingType;

    @Column(nullable = false)
    private float priceAtBookingTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookedIn bookedIn = (BookedIn) o;
        return Float.compare(bookedIn.priceAtBookingTime, priceAtBookingTime) == 0
            && Objects.equals(id, bookedIn.id)
            && Objects.equals(transaction, bookedIn.transaction)
            && Objects.equals(ticket, bookedIn.ticket)
            && bookingType == bookedIn.bookingType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookingType, priceAtBookingTime);
    }

    @Override
    public String toString() {
        return "BookedIn{"
            + "id="
            + id
            + ", transaction="
            + transaction
            + ", ticket="
            + ticket
            + ", bookingType="
            + bookingType
            + ", priceAtBookingTime="
            + priceAtBookingTime
            + '}';
    }

    public BookedInKey getId() {
        return id;
    }

    public void setId(BookedInKey id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public BookingType getBookingType() {
        return bookingType;
    }

    public void setBookingType(BookingType bookingType) {
        this.bookingType = bookingType;
    }

    public float getPriceAtBookingTime() {
        return priceAtBookingTime;
    }

    public void setPriceAtBookingTime(float priceAtBookingTime) {
        this.priceAtBookingTime = priceAtBookingTime;
    }
}
