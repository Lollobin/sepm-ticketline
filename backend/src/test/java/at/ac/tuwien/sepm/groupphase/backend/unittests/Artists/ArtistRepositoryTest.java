package at.ac.tuwien.sepm.groupphase.backend.unittests.Artists;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_LASTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_LASTNAME;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void searchWithCorrectParams_shouldReturnCorrectArtists(){

        Artist artist = new Artist();
        artist.setFirstName(ARTIST_FIRSTNAME);
        artist.setLastName(ARTIST_LASTNAME);
        artist.setKnownAs(ARTIST_KNOWNAS);
        artist.setBandName(ARTIST_BANDNAME);

        Artist artist2 = new Artist();
        artist2.setFirstName(ARTIST2_FIRSTNAME);
        artist2.setLastName(ARTIST2_LASTNAME);
        artist2.setKnownAs(ARTIST2_KNOWNAS);
        artist2.setBandName(ARTIST2_BANDNAME);

        artistRepository.save(artist);
        artistRepository.save(artist2);

        assertNotNull(artist.getArtistId());
        assertThat(artist).hasFieldOrPropertyWithValue("firstName", ARTIST_FIRSTNAME)
            .hasFieldOrPropertyWithValue("lastName", ARTIST_LASTNAME)
            .hasFieldOrPropertyWithValue("knownAs", ARTIST_KNOWNAS)
            .hasFieldOrPropertyWithValue("bandName", ARTIST_BANDNAME);

        assertNotNull(artist2.getArtistId());
        assertThat(artist2).hasFieldOrPropertyWithValue("firstName", ARTIST2_FIRSTNAME)
            .hasFieldOrPropertyWithValue("lastName", ARTIST2_LASTNAME)
            .hasFieldOrPropertyWithValue("knownAs", ARTIST2_KNOWNAS)
            .hasFieldOrPropertyWithValue("bandName", ARTIST2_BANDNAME);

        Page<Artist> artists = artistRepository.search("", PageRequest.of(0, 10, Sort.by("id").ascending()));

        Artist persistedArtist = artists.getContent().get(0);
        Artist persistedArtist2 = artists.getContent().get(1);

        assertThat(persistedArtist).hasFieldOrPropertyWithValue("firstName", ARTIST_FIRSTNAME)
            .hasFieldOrPropertyWithValue("lastName", ARTIST_LASTNAME)
            .hasFieldOrPropertyWithValue("knownAs", ARTIST_KNOWNAS)
            .hasFieldOrPropertyWithValue("bandName", ARTIST_BANDNAME);

        assertThat(persistedArtist2).hasFieldOrPropertyWithValue("firstName", ARTIST2_FIRSTNAME)
            .hasFieldOrPropertyWithValue("lastName", ARTIST2_LASTNAME)
            .hasFieldOrPropertyWithValue("knownAs", ARTIST2_KNOWNAS)
            .hasFieldOrPropertyWithValue("bandName", ARTIST2_BANDNAME);

        artists = artistRepository.search(ARTIST_FIRSTNAME.substring(1,ARTIST_FIRSTNAME.length() - 2),
            PageRequest.of(0, 10, Sort.by("id").ascending()));

        persistedArtist = artists.getContent().get(0);

        assertThat(persistedArtist).hasFieldOrPropertyWithValue("firstName", ARTIST_FIRSTNAME)
            .hasFieldOrPropertyWithValue("lastName", ARTIST_LASTNAME)
            .hasFieldOrPropertyWithValue("knownAs", ARTIST_KNOWNAS)
            .hasFieldOrPropertyWithValue("bandName", ARTIST_BANDNAME);

        artists = artistRepository.search(ARTIST2_KNOWNAS.substring(1,ARTIST2_KNOWNAS.length() - 2),
            PageRequest.of(0, 10, Sort.by("id").ascending()));

        persistedArtist = artists.getContent().get(0);

        assertThat(persistedArtist).hasFieldOrPropertyWithValue("firstName", ARTIST2_FIRSTNAME)
            .hasFieldOrPropertyWithValue("lastName", ARTIST2_LASTNAME)
            .hasFieldOrPropertyWithValue("knownAs", ARTIST2_KNOWNAS)
            .hasFieldOrPropertyWithValue("bandName", ARTIST2_BANDNAME);

        artists = artistRepository.search(ARTIST_FIRSTNAME.substring(1)+ " " + ARTIST_LASTNAME,
            PageRequest.of(0, 10, Sort.by("id").ascending()));

        persistedArtist = artists.getContent().get(0);

        assertThat(persistedArtist).hasFieldOrPropertyWithValue("firstName", ARTIST_FIRSTNAME)
            .hasFieldOrPropertyWithValue("lastName", ARTIST_LASTNAME)
            .hasFieldOrPropertyWithValue("knownAs", ARTIST_KNOWNAS)
            .hasFieldOrPropertyWithValue("bandName", ARTIST_BANDNAME);

        artistRepository.deleteAll();

    }

    @Test
    void searchWithFalseParams_shouldReturnNoArtists(){

        Page<Artist> artists = artistRepository.search("NOT EXISTING",
            PageRequest.of(0, 10, Sort.by("id").ascending()));

        assertThat(artists).isEmpty();

        artistRepository.deleteAll();
    }
}
