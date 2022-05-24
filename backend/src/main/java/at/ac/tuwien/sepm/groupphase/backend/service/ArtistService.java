package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistsSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.domain.Pageable;

public interface ArtistService {

    /**
     * Find a single artist entry by id.
     *
     * @param id the id of the artist entry
     * @return the artist entry
     */
    Artist findOne(Long id);

    /**
     * Searches the database for artists which correspond to the given search String.
     *
     * @param search for which we search
     * @param pageable contains information about the page we request
     * @return found ArtistsSearchResultDto from database
     */
    ArtistsSearchResultDto search(String search, Pageable pageable);

    /**
     * Finds all artists in database.
     *
     * @param pageable contains information about the page we request
     * @return found ArtistsSearchResultDto from database
     */
    ArtistsSearchResultDto findAll(Pageable pageable);
}
