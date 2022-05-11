package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ArtistsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${openapi.ticketline.base-path:}")
public class ArtistsEndpoint implements ArtistsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    public ArtistsEndpoint(ArtistService artistService, ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }

    @Override
    public ResponseEntity<ArtistDto> artistsIdGet(Long id) {
        LOGGER.info("GET artists/{}", id);
        ArtistDto artist = this.artistMapper.artistToArtistDto(
            artistService.findOne(id));
        return ResponseEntity.ok(artist);
    }
}
