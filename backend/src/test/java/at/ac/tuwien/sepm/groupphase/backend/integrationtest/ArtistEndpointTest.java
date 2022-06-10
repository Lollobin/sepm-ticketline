package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST2_LASTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST3_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST3_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST3_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST3_LASTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_LASTNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistsSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ArtistEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ArtistRepository artistRepository;

    @BeforeEach
    public void beforeEach() {
        artistRepository.deleteAll();
    }

    @Test
    void get_shouldReturnArtistsWithFirstNamePatrickAndLastNameGrossmann() throws Exception {

        saveThreeArtistsInRepo();

        String firstName = "patrick";
        String lastName = "grossmann";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/artists").param("search", firstName + " " + lastName)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        ArtistsSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), ArtistsSearchResultDto.class);

        assertThat(searchResultDto.getArtists()).hasSize(1);
        assertThat(searchResultDto.getArtists().get(0).getFirstName()).isEqualTo(ARTIST3_FIRSTNAME);
        assertThat(searchResultDto.getArtists().get(0).getLastName()).isEqualTo(ARTIST3_LASTNAME);


    }

    @Test
    void get_shouldReturnArtistWithKnownAsFerbl() throws Exception {

        saveThreeArtistsInRepo();

        String knownAs = "ferbl";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/artists").param("search", knownAs)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        ArtistsSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), ArtistsSearchResultDto.class);

        assertThat(searchResultDto.getArtists()).hasSize(1);
        assertThat(searchResultDto.getArtists().get(0).getFirstName()).isEqualTo(ARTIST_FIRSTNAME);
        assertThat(searchResultDto.getArtists().get(0).getLastName()).isEqualTo(ARTIST_LASTNAME);
    }

    @Test
    void get_shouldNotReturnAnyDueToNotMatchingBandName() throws Exception {

        saveThreeArtistsInRepo();

        String bandName = "kerdies Jungs";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/artists").param("search", bandName)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        ArtistsSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), ArtistsSearchResultDto.class);

        assertThat(searchResultDto.getArtists()).isEmpty();
    }

    @Test
    void get_shouldReturnArtistsWithBandNameFerdies() throws Exception {

        saveThreeArtistsInRepo();

        String bandName = "ferdies";

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/artists").param("search", bandName)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        ArtistsSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), ArtistsSearchResultDto.class);

        assertThat(searchResultDto.getArtists()).hasSize(2);
        assertThat(searchResultDto.getArtists().get(0).getFirstName()).isEqualTo(ARTIST_FIRSTNAME);
        assertThat(searchResultDto.getArtists().get(0).getLastName()).isEqualTo(ARTIST_LASTNAME);

        assertThat(searchResultDto.getArtists().get(1).getFirstName()).isEqualTo(ARTIST2_FIRSTNAME);
        assertThat(searchResultDto.getArtists().get(1).getLastName()).isEqualTo(ARTIST2_LASTNAME);

        assertThat(searchResultDto.getArtists().get(0).getBandName()).isEqualTo(ARTIST_BANDNAME);
        assertThat(searchResultDto.getArtists().get(1).getBandName()).isEqualTo(ARTIST2_BANDNAME);

    }

    @Test
    void get_shouldReturnArtistWithFirstNameAndLastNameAndKnownAs() throws Exception {

        saveThreeArtistsInRepo();

        String firstName = "friedrich";
        String lastName = "bauer";
        String knownAs = "fried";

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/artists")
            .param("search", firstName + " " + lastName + " " + knownAs)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        ArtistsSearchResultDto searchResultDto = objectMapper.readValue(
            servletResponse.getContentAsString(), ArtistsSearchResultDto.class);

        assertThat(searchResultDto.getArtists()).hasSize(1);
        assertThat(searchResultDto.getArtists().get(0).getFirstName()).isEqualTo(ARTIST2_FIRSTNAME);
        assertThat(searchResultDto.getArtists().get(0).getLastName()).isEqualTo(ARTIST2_LASTNAME);
        assertThat(searchResultDto.getArtists().get(0).getKnownAs()).isEqualTo(ARTIST2_KNOWNAS);
    }


    private void saveThreeArtistsInRepo() {
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

        Artist artist3 = new Artist();
        artist3.setFirstName(ARTIST3_FIRSTNAME);
        artist3.setLastName(ARTIST3_LASTNAME);
        artist3.setKnownAs(ARTIST3_KNOWNAS);
        artist3.setBandName(ARTIST3_BANDNAME);

        artistRepository.save(artist);
        artistRepository.save(artist2);
        artistRepository.save(artist3);
    }

}
