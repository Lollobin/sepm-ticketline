package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.util.List;

public interface ShowService {

    /**
     * saves given show entity object to EventRepository
     *
     * @param show to be saved
     * @return entity show object that was saved with new id
     */
    Show createShow(Show show);

    List<Show> findAll();
}
