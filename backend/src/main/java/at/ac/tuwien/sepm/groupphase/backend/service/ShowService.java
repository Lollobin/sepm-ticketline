package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;

public interface ShowService {
    /**
     * Find a single show entry by id.
     *
     * @param id the id of the show entry
     * @return the show entry
     */
    Show findOne(Long id);
}
