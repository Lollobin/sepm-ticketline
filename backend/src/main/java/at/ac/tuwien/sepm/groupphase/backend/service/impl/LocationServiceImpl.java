package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchResultDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.LocationService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.SearchValidator;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final LocationRepository locationRepository;

    private final SearchValidator searchValidator;

    private final LocationMapper locationMapper;

    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper,
        SearchValidator searchValidator) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.searchValidator = searchValidator;
    }

    @Override
    public LocationSearchResultDto search(LocationSearchDto searchDto, Pageable pageable) {

        LOGGER.debug("getting location with pageable: {}", pageable);

        searchValidator.validateLocationSearchDto(searchDto);

        checkForBlankValues(searchDto);

        Page<Location> locationPage = locationRepository.search(searchDto.getName(),
            searchDto.getCity(), searchDto.getCountry(), searchDto.getStreet(),
            searchDto.getZipCode(), pageable);

        return setLocationSearchDto(locationPage);

    }

    private void checkForBlankValues(LocationSearchDto locationSearchDto) {
        LOGGER.trace("setting blank values to null");

        if (locationSearchDto.getZipCode() != null && locationSearchDto.getZipCode().isBlank()) {
            locationSearchDto.setZipCode(null);
        }
        if (locationSearchDto.getCountry() != null && locationSearchDto.getCountry().isBlank()) {
            locationSearchDto.setCountry(null);
        }
        if (locationSearchDto.getStreet() != null && locationSearchDto.getStreet().isBlank()) {
            locationSearchDto.setStreet(null);
        }
        if (locationSearchDto.getName() != null && locationSearchDto.getName().isBlank()) {
            locationSearchDto.setName(null);
        }
        if (locationSearchDto.getCity() != null && locationSearchDto.getCity().isBlank()) {
            locationSearchDto.setCity(null);
        }
    }

    private LocationSearchResultDto setLocationSearchDto(Page<Location> locationPage) {
        LOGGER.trace("setting location result dto");
        LocationSearchResultDto searchResultDto = new LocationSearchResultDto();

        searchResultDto.setLocations(
            locationPage.getContent().stream().map(locationMapper::locationToLocationDto).toList());
        searchResultDto.setNumberOfResults(locationPage.getNumberOfElements());
        searchResultDto.setCurrentPage(locationPage.getNumber());
        searchResultDto.setPagesTotal(locationPage.getTotalPages());

        return searchResultDto;
    }

    @Override
    public LocationSearchResultDto findAll(Pageable pageable) {
        LOGGER.trace("Getting all locations");

        Page<Location> locationPage = locationRepository.findAll(pageable);

        return setLocationSearchDto(locationPage);

    }
}
