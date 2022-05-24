package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchResultDto;
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
     *  * Return a page of locations.
     *
     * @param pageable contains informations about the page
     * @return Dto with list of locations and page information
     */
    LocationSearchResultDto findAll(Pageable pageable);

}
