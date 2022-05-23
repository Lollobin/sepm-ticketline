package at.ac.tuwien.sepm.groupphase.backend.entity.embeddables;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class PlaysInId implements Serializable {

    private Long showId;
    private Long artistId;

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlaysInId playsInId = (PlaysInId) o;
        return Objects.equals(showId, playsInId.showId) && Objects.equals(artistId,
            playsInId.artistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showId, artistId);
    }

    @Override
    public String toString() {
        return "PlaysInId{"
            + "showId=" + showId
            + ", artistId=" + artistId
            + '}';
    }

}
