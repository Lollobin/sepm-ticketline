package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.PlaysInId;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class PlaysIn {

    @EmbeddedId
    PlaysInId id = new PlaysInId();

    @ManyToOne
    @MapsId("showId")
    @JoinColumn(name = "showId")
    private Show show;

    @ManyToOne
    @MapsId("artistId")
    @JoinColumn(name = "artistId")
    private Artist artist;

    public PlaysInId getId() {
        return id;
    }

    public void setId(PlaysInId id) {
        this.id = id;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PlaysIn playsIn = (PlaysIn) o;
        return Objects.equals(id, playsIn.id) && Objects.equals(show, playsIn.show)
            && Objects.equals(artist, playsIn.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, show, artist);
    }

    @Override
    public String toString() {
        return "PlaysIn{"
            + "id=" + id
            + ", show=" + show
            + ", artist=" + artist
            + '}';
    }
}
