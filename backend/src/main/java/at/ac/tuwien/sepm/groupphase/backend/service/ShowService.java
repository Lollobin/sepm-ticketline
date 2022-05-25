package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorPriceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import java.util.List;

public interface ShowService {

    /**
     * saves given show entity object to ShowRepository.
     *
     * @param show to be saved
     * @param seatingPlanId that is associated with the show
     * @param sectorPriceDtos List of sectorPriceDtos that are associated with the seatingPlan
     * @return entity show object that was saved with new id
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException if the seatingPlan,
     *     the event, the artists or the seats of the seatingPlan are not present in the database
     * @throws at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException if the sectorPrices
     *     don't correlate with the sectors in the database
     */
    Show createShow(Show show, Long seatingPlanId, List<SectorPriceDto> sectorPriceDtos);

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
