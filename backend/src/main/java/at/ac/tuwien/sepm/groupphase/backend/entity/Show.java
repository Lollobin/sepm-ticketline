package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;

    private LocalDateTime date;

    @ManyToMany
    @JoinTable(
        name = "PlaysIn",
        joinColumns = @JoinColumn(name = "showId"),
        inverseJoinColumns = @JoinColumn(name = "artistId")
    )
    private Set<Artist> artistIds;

    @ManyToOne
    @JoinColumn(name = "eventId",
        referencedColumnName = "eventId",
        nullable = false
    )
    private Event event;

    @Override
    public String toString() {
        return "Show{" +
            "showId=" + showId +
            ", date=" + date +
            ", artistIds=" + artistIds +
            ", event=" + event +
            '}';
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
        return showId == show.showId && Objects.equals(date, show.date)
            && Objects.equals(artistIds, show.artistIds) && Objects.equals(event,
            show.event);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showId, date, artistIds, event);
    }

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Set<Artist> getArtistIds() {
        return artistIds;
    }

    public void setArtistIds(Set<Artist> artistIds) {
        this.artistIds = artistIds;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
