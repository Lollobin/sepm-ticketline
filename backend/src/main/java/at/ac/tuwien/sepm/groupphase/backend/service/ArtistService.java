package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistService {

    /**
     * Find a single artist entry by id.
     *
     * @param id the id of the artist entry
     * @return the artist entry
     */
    Artist findOne(Long id);

    Page<Artist> search(String search, Pageable pageable);
}
