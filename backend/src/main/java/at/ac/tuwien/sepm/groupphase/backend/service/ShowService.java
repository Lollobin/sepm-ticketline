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
     * finds show with given id in ShowRepository.
     *
     * @param id of searched show
     * @return show with given id
     */
    Show findById(Long id);
}
