package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SortDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.interfaces.LocationsApi;
import at.ac.tuwien.sepm.groupphase.backend.service.LocationService;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationsEndpoint implements LocationsApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final LocationService locationService;

    public LocationsEndpoint(LocationService locationService) {
        this.locationService = locationService;
    }


    @Override
    public ResponseEntity<LocationSearchResultDto> locationsGet(LocationSearchDto search,
        Integer pageSize, Integer requestedPage, SortDto sort) {
        LOGGER.info("GET /locations with search: {}", search);

        Pageable pageable = PageRequest.of(requestedPage, pageSize, Direction.fromString(sort.getValue()),
            "name");
        LocationSearchResultDto searchResultDto;
        if ((search.getName() == null || search.getName().isBlank()) && (search.getCountry() == null
            || search.getCountry().isBlank()) && (search.getCity() == null || search.getCity()
            .isBlank()) && (search.getStreet() == null || search.getStreet().isBlank()) && (
            search.getZipCode() == null || search.getZipCode().isBlank())) {
            searchResultDto = locationService.findAll(pageable);
        } else {
            searchResultDto = locationService.search(search, pageable);
        }

        return ResponseEntity.ok(searchResultDto);

    }
}
