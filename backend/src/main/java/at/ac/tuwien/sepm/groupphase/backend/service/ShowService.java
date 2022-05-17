package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.util.List;

public interface ShowService {

    /**
     * saves given show entity object to ShowRepository.
     *
     * @param show to be saved
     * @return entity show object that was saved with new id
     */
    Show createShow(Show show);

    /**
     * finds all shows in ShowRepository.
     *
     * @return list of all shows
     */
    List<Show> findAll();

    /**
     * Find a single show entry by id.
     *
     * @param id the id of the show entry
     * @return the show entry
     */
    Show findOne(Long id);
}
