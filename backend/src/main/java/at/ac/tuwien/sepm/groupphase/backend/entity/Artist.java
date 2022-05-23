package at.ac.tuwien.sepm.groupphase.backend.entity;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;

    private String bandName;

    private String knownAs;

    private String firstName;

    private String lastName;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @Cascade({ CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DELETE })
    @JoinTable(name = "PlaysIn", joinColumns = @JoinColumn(name = "artistId"), inverseJoinColumns = @JoinColumn(name = "showId"))
    private Set<Show> shows;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Artist artist = (Artist) o;
        return artistId == artist.artistId
            && Objects.equals(bandName, artist.bandName)
            && Objects.equals(knownAs, artist.knownAs)
            && Objects.equals(firstName, artist.firstName)
            && Objects.equals(lastName, artist.lastName)
            && Objects.equals(shows, artist.shows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistId, bandName, knownAs, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Artist{"
            + "artistId="
            + artistId
            + ", bandName='"
            + bandName
            + '\''
            + ", knownAs='"
            + knownAs
            + '\''
            + ", firstName='"
            + firstName
            + '\''
            + ", lastName='"
            + lastName
            + '}';
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getKnownAs() {
        return knownAs;
    }

    public void setKnownAs(String knownAs) {
        this.knownAs = knownAs;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Show> getShows() {
        return shows;
    }

    public void setShows(Set<Show> shows) {
        this.shows = shows;
    }
}
