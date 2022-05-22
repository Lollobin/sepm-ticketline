package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class ArtistDataGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ArtistRepository artistRepository;
    private final Faker faker = new Faker();

    public ArtistDataGenerator(
        ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public void generateArtists(int numberOfArtists) {
        if (!artistRepository.findAll().isEmpty()) {
            LOGGER.debug("artists already generated");
            return;
        }

        LOGGER.debug("generating {} artists", numberOfArtists);

        for (int i = 0; i < numberOfArtists; i++) {
            Artist artist = new Artist();
            String bandName = faker.rockBand().name();
            artist.setBandName(bandName);
            artist.setKnownAs(bandName);
            artist.setFirstName(faker.name().firstName());
            artist.setLastName(faker.name().lastName());
            LOGGER.debug("saving artist {}", artist);
            artistRepository.save(artist);
        }
    }
}
