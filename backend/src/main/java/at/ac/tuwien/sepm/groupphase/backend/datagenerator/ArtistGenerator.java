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
public class ArtistGenerator {

    private static final Logger LOGGER =
        LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ArtistRepository artistRepository;
    private final Faker faker = new Faker();

    public ArtistGenerator(
        ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public void generateData(int numberOfArtists) {
        if (!artistRepository.findAll().isEmpty()) {
            LOGGER.debug("artists already generated");
            return;
        }

        LOGGER.debug("generating {} artists", numberOfArtists);

        for (int i = 0; i < numberOfArtists; i++) {
            Artist artist = generateArtist();
            LOGGER.trace("saving artist {}", artist);
            artistRepository.save(artist);
        }
    }

    public Artist generateArtist() {
        Artist artist = new Artist();
        double random = faker.number().randomDouble(2, 0, 1);
        if (random > 0.5) {
            artist.setBandName(faker.rockBand().name());
        } else {
            if (random < 0.25) {
                artist.setKnownAs(faker.hobbit().character());
            }
            artist.setFirstName(faker.name().firstName());
            artist.setLastName(faker.name().lastName());
        }
        return artist;
    }
}
