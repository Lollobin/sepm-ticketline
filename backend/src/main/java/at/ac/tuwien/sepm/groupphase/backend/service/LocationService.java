package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface LocationService {

    /**
     * Returns a page of locations that match the params of the param searchDto.
     *
     * @param searchDto contains the parameters to search for
     * @param pageable  contains information about the page
     * @return Dto with list of locations and page information
     */
    LocationSearchResultDto search(LocationSearchDto searchDto, Pageable pageable);

    /**
     * Return a page of locations.
     *
     * @param pageable contains informations about the page
     * @return Dto with list of locations and page information
     */
    LocationSearchResultDto findAll(Pageable pageable);

    List<SeatingPlanDto> findSeatingPlans(Long id);

    /**
     * Saves a location.
     *
     * @param location to be saved
     * @return location object that was saved
     */
    Location saveLocation(Location location);

    /**
     * Finds a location by its id.
     *
     * @param id The id that will be searched for
     * @return The found location with the given ID
     */
    Location findOne(Long id);
}
