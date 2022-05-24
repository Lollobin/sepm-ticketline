package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistsSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.ArtistsApi;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
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
        ArtistDto artist = this.artistMapper.artistToArtistDto(artistService.findOne(id));
        return ResponseEntity.ok(artist);
    }

    @Override
    public ResponseEntity<ArtistsSearchResultDto> artistsGet(String search, Integer pageSize,
        Integer requestedPage, String sort) {
        LOGGER.info("GET artists/ with search string: {}", search);

        Pageable pageable = PageRequest.of(requestedPage, pageSize, Direction.fromString(sort),
            "knownAs");

        ArtistsSearchResultDto searchResultDto;

        if (search == null || search.isBlank()) {
            searchResultDto = artistService.findAll(pageable);
        } else {
            searchResultDto = artistService.search(search, pageable);
        }

        return ResponseEntity.ok(searchResultDto);

    }

}
