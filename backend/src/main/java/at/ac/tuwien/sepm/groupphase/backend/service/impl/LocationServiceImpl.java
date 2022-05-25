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

        LOGGER.trace("getting location with pageable: {}", pageable);

        if (checkIfAllFieldNullOrBlank(searchDto)) {
            return findAll(pageable);
        } else {

            resetBlankValuesToNull(searchDto);

            searchValidator.validateLocationSearchDto(searchDto);

            Page<Location> locationPage = locationRepository.search(searchDto.getName(),
                searchDto.getCity(), searchDto.getCountry(), searchDto.getStreet(),
                searchDto.getZipCode(), pageable);

            return setLocationSearchDto(locationPage);
        }


    }

    private boolean checkIfAllFieldNull(LocationSearchDto search) {
        LOGGER.trace("checking if all fields are null");
        return search.getCity() == null && search.getCountry() == null && search.getName() == null
            && search.getStreet() == null && search.getZipCode() == null;

    }

    private void resetBlankValuesToNull(LocationSearchDto searchDto) {
        LOGGER.trace("setting blank values to null");
        if (searchDto.getCity() != null && searchDto.getCity().isBlank()) {
            searchDto.setCity(null);
        }

        if (searchDto.getName() != null && searchDto.getName().isBlank()) {
            searchDto.setName(null);
        }

        if (searchDto.getStreet() != null && searchDto.getStreet().isBlank()) {
            searchDto.setStreet(null);
        }

        if (searchDto.getCountry() != null && searchDto.getCountry().isBlank()) {
            searchDto.setCountry(null);
        }
        if (searchDto.getZipCode() != null && searchDto.getZipCode().isBlank()) {
            searchDto.setZipCode(null);
        }
    }

    private boolean checkIfAllFieldNullOrBlank(LocationSearchDto search) {
        if (checkIfAllFieldNull(search)) {
            return true;
        } else {
            LOGGER.debug("Not all fields are null, checking if all fields are blank");
            return (search.getCity() != null && search.getCity().isBlank()) && (
                search.getCountry() != null && search.getCountry().isBlank()) && (
                search.getName() != null && search.getName().isBlank()) && (
                search.getStreet() != null && search.getStreet().isBlank()) && (
                search.getZipCode() != null && search.getZipCode().isBlank());
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
