package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long transactionId;

    @Column(nullable = false)
    private LocalDate date;

    private String billPath;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = false)
    private ApplicationUser user;

    @OneToMany(mappedBy = "transaction")
    private Set<BookedIn> bookedIns;

    @Override
    public String toString() {
        return "Transaction{" + "transactionId=" + transactionId + ", date=" + date + ", billPath='"
            + billPath + '\'' + ", user=" + user + ", bookedIns=" + bookedIns + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId && Objects.equals(date, that.date)
            && Objects.equals(billPath, that.billPath) && Objects.equals(user, that.user)
            && Objects.equals(bookedIns, that.bookedIns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, date, billPath, user, bookedIns);
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBillPath() {
        return billPath;
    }

    public void setBillPath(String billPath) {
        this.billPath = billPath;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public Set<BookedIn> getBookedIns() {
        return bookedIns;
    }

    public void setBookedIns(Set<BookedIn> bookedIns) {
        this.bookedIns = bookedIns;
    }
}
