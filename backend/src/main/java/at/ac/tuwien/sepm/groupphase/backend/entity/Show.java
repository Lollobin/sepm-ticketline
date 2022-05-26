package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    @Column
    private OffsetDateTime date;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @Cascade({CascadeType.MERGE, CascadeType.DELETE})
    @JoinTable(name = "PlaysIn", joinColumns = @JoinColumn(name = "showId"), inverseJoinColumns = @JoinColumn(name = "artistId"))
    private Set<Artist> artists;

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @Cascade(CascadeType.MERGE)
    @JoinColumn(name = "eventId", referencedColumnName = "eventId", nullable = false)
    private Event event;

    @OneToMany(mappedBy = "show")
    private Set<SectorPrice> sectorPrices;

    @Override
    public String toString() {
        return "Show{" + "showId=" + showId + ", date=" + date + ", artistIds=" + artists
            + ", event=" + event + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Show show = (Show) o;
        return Objects.equals(showId, show.showId) && Objects.equals(date, show.date)
            && Objects.equals(artists,
            show.artists) && Objects.equals(event, show.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showId, date);
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Set<Artist> getArtists() {
        return artists;
    }

    public void setArtists(Set<Artist> artists) {
        this.artists = artists;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Set<SectorPrice> getSectorPrices() {
        return sectorPrices;
    }

    public void setSectorPrices(
        Set<SectorPrice> sectorPrices) {
        this.sectorPrices = sectorPrices;
    }
}
