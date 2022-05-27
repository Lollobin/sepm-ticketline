package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface LocationMapper {

    @Mapping(target = "locationId", source = "id")
    LocationDto locationToLocationDto(Location location);

    @Mapping(target = "id", source = "locationId")
    Location locationDtoToLocation(LocationDto locationDto);
}
