package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.mapstruct.Mapper;

@Mapper
public interface SeatMapper {

    Seat seatDtoToSeat(SeatDto seatDto);

    default Sector map(Long value) {
        Sector sector = new Sector();
        sector.setSectorId(value);
        return sector;
    }

    ;
}
