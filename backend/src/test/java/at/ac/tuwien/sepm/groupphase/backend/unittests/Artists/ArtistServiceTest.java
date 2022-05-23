package at.ac.tuwien.sepm.groupphase.backend.unittests.Artists;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_LASTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_INVALIDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_LASTNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistsSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ArtistServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Spy
    private ArtistMapper artistMapper = Mappers.getMapper(ArtistMapper.class);
    private ArtistService artistService;
    Pageable pageable = PageRequest.of(0, 10, Direction.fromString("ASC"), "firstName");

    @BeforeEach
    void setUp() {
        artistService = new ArtistServiceImpl(artistRepository, artistMapper);
        artistRepository.deleteAll();
    }

    @Test
    void findAllWithStandardPageable_shouldReturnAllArtists() {

        Artist artist1 = new Artist();
        artist1.setFirstName(ARTIST_FIRSTNAME);
        artist1.setLastName(ARTIST_LASTNAME);
        artist1.setBandName(ARTIST_BANDNAME);
        artist1.setKnownAs(ARTIST_KNOWNAS);

        Artist artist2 = new Artist();
        artist2.setFirstName(ARTIST2_FIRSTNAME);
        artist2.setLastName(ARTIST2_LASTNAME);
        artist2.setBandName(ARTIST2_BANDNAME);
        artist2.setKnownAs(ARTIST2_KNOWNAS);

        List<Artist> artists = new ArrayList<>();
        artists.add(artist1);
        artists.add(artist2);

        Page<Artist> artistPage = new PageImpl<>(artists);

        when(artistRepository.findAll(pageable)).thenReturn(artistPage);

        ArtistsSearchResultDto artistsSearchResultDto = artistService.findAll(pageable);

        assertAll(
            () -> assertEquals(artistsSearchResultDto.getArtists().size(), 2),
            () -> assertEquals(artistsSearchResultDto.getArtists().get(0).getFirstName(), artist1.getFirstName()),
            () -> assertEquals(artistsSearchResultDto.getArtists().get(0).getLastName(), artist1.getLastName()),
            () -> assertEquals(artistsSearchResultDto.getArtists().get(1).getFirstName(), artist2.getFirstName()),
        () -> assertEquals(artistsSearchResultDto.getArtists().get(1).getLastName(), artist2.getLastName())
        );

        artists.clear();
        artistPage = new PageImpl<>(artists);
        when(artistRepository.findAll(pageable)).thenReturn(artistPage);

        ArtistsSearchResultDto emptyResult = artistService.findAll(pageable);

        assertThat(emptyResult.getArtists()).isEmpty();
    }

    @Test
    void searchWithStandardPageable_shouldReturnCorrectArtists() {

        Artist artist1 = new Artist();
        artist1.setFirstName(ARTIST_FIRSTNAME);
        artist1.setLastName(ARTIST_LASTNAME);
        artist1.setBandName(ARTIST_BANDNAME);
        artist1.setKnownAs(ARTIST_KNOWNAS);

        Artist artist2 = new Artist();
        artist2.setFirstName(ARTIST2_FIRSTNAME);
        artist2.setLastName(ARTIST2_LASTNAME);
        artist2.setBandName(ARTIST2_BANDNAME);
        artist2.setKnownAs(ARTIST2_KNOWNAS);

        List<Artist> artists1 = new ArrayList<>();
        artists1.add(artist1);

        List<Artist> artists2 = new ArrayList<>();
        artists1.add(artist2);

        List<Artist> artists3 = new ArrayList<>();
        artists3.add(artist1);
        artists3.add(artist2);

        Pageable pageable = PageRequest.of(0, 10, Direction.fromString("ASC"), "firstName");

        Page<Artist> artistPage1 = new PageImpl<>(artists1);
        Page<Artist> artistPage2 = new PageImpl<>(artists2);
        Page<Artist> artistPage3 = new PageImpl<>(artists3);
        Page<Artist> artistPage4 = new PageImpl<>(new ArrayList<>());

        when(artistRepository.search(ARTIST_FIRSTNAME, pageable)).thenReturn(artistPage1);
        when(artistRepository.search(ARTIST2_FIRSTNAME, pageable)).thenReturn(artistPage2);
        when(artistRepository.search(ARTIST_BANDNAME, pageable)).thenReturn(artistPage3);
        when(artistRepository.search(ARTIST_INVALIDNAME, pageable)).thenReturn(artistPage4);

        ArtistsSearchResultDto artistsSearchResultDto1 = artistService.search(ARTIST_FIRSTNAME, pageable);
        ArtistsSearchResultDto artistsSearchResultDto2 = artistService.search(ARTIST2_BANDNAME, pageable);
        ArtistsSearchResultDto artistsSearchResultDto3 = artistService.search(ARTIST_BANDNAME, pageable);
        ArtistsSearchResultDto artistsSearchResultDto4 = artistService.search(ARTIST_INVALIDNAME, pageable);

        assertAll(
            () -> assertEquals(artistsSearchResultDto1.getArtists().size(), 1),
            () -> assertEquals(artistsSearchResultDto1.getArtists().get(0).getFirstName(), artist1.getFirstName()),
            () -> assertEquals(artistsSearchResultDto1.getArtists().get(0).getLastName(), artist1.getLastName()),
            () -> assertEquals(artistsSearchResultDto2.getArtists().size(), 1),
            () -> assertEquals(artistsSearchResultDto2.getArtists().get(0).getFirstName(), artist2.getFirstName()),
            () -> assertEquals(artistsSearchResultDto2.getArtists().get(0).getLastName(), artist2.getLastName()),
            () -> assertEquals(artistsSearchResultDto3.getArtists().size(), 2),
            () -> assertEquals(artistsSearchResultDto3.getArtists().get(0).getFirstName(), artist1.getFirstName()),
            () -> assertEquals(artistsSearchResultDto3.getArtists().get(0).getLastName(), artist1.getLastName()),
            () -> assertEquals(artistsSearchResultDto3.getArtists().get(1).getFirstName(), artist2.getFirstName()),
            () -> assertEquals(artistsSearchResultDto3.getArtists().get(1).getLastName(), artist2.getLastName()),
            () -> assertThat(artistsSearchResultDto4.getArtists()).isEmpty()
        );
    }
}
