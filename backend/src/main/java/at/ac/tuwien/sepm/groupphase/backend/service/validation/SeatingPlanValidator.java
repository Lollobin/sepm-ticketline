package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ProvisionalSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanLayoutDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class SeatingPlanValidator {

    void checkSeatingPlanValid(SeatingPlanWithoutIdDto seatingPlanWithoutId) {
        if (seatingPlanWithoutId.getName().isBlank()) {
            throw new ValidationException("A valid name must be set for the seating plan");
        }
        checkSectorsValid(seatingPlanWithoutId.getSectors());
        checkSeatsValid(seatingPlanWithoutId);
        checkLayoutValid(seatingPlanWithoutId);
    }

    void checkLayoutValid(SeatingPlanWithoutIdDto seatingPlanWithoutId) {
        Map<Long, ProvisionalSectorDto> sectorMap = new HashMap<>();
        for (ProvisionalSectorDto sector : seatingPlanWithoutId.getSectors()) {
            sectorMap.put(sector.getId(), sector);
        }
        Map<Long, SeatDto> seatMap = new HashMap<>();
        for (SeatDto seat : seatingPlanWithoutId.getSeats()) {
            seatMap.put(seat.getId(), seat);
        }
        checkLayoutSectorsValid(seatingPlanWithoutId.getSeatingPlanLayout().getSectors(),
            sectorMap);
        checkLayoutSeatsValid(seatingPlanWithoutId.getSeatingPlanLayout(), seatMap);
    }


    void checkLayoutSectorsValid(List<SeatingPlanSectorDto> sectors,
        Map<Long, ProvisionalSectorDto> sectorMap) {
        Set<Long> existingIds = new HashSet<>();
        List<Long> duplicates = new ArrayList<>();
        for (SeatingPlanSectorDto sectorDto : sectors) {
            if (!existingIds.add(sectorDto.getId())) {
                duplicates.add(sectorDto.getId());
            }
            if (duplicates.isEmpty()) {
                checkLayoutSectorValid(sectorDto);
                if (Objects.equals(sectorMap.get(sectorDto.getId()), null)) {
                    throw new ValidationException(
                        "The layout contains a sector with the ID " + sectorDto.getId()
                            + ", which is not present in the sectors on the top level");
                }
            }
        }
        if (!duplicates.isEmpty()) {
            throw new ValidationException(
                "All sector IDs must be unique. Found non-unique values:" + Arrays.toString(
                    duplicates.toArray()));
        }
    }

    void checkLayoutSectorValid(SeatingPlanSectorDto sector) {
        if (sector.getNoSeats() && Objects.equals(sector.getLocation(), null)) {
            throw new ValidationException(
                "Sector " + sector.getId()
                    + " must have a location, because it is not a standing sector");
        }
        if (!sector.getNoSeats() && !Objects.equals(sector.getLocation(), null)) {
            throw new ValidationException(
                "Sector " + sector.getId()
                    + " is not allowed to have a location, because it is a standing sector");
        }
    }

    void checkLayoutSeatsValid(SeatingPlanLayoutDto seatingPlanLayoutDto,
        Map<Long, SeatDto> seatDtoMap) {
        Map<Long, SeatingPlanSectorDto> seatingPlanSectorMap = new HashMap<>();
        for (SeatingPlanSectorDto sector : seatingPlanLayoutDto.getSectors()) {
            seatingPlanSectorMap.put(sector.getId(), sector);
        }
        Set<Long> existingIds = new HashSet<>();
        List<Long> duplicates = new ArrayList<>();
        for (SeatingPlanSeatDto seatDto : seatingPlanLayoutDto.getSeats()) {
            if (!existingIds.add(seatDto.getId())) {
                duplicates.add(seatDto.getId());
            }
            if (duplicates.isEmpty()) {
                checkLayoutSeatValid(seatDto, seatingPlanSectorMap);
                if (Objects.equals(seatDtoMap.get(seatDto.getId()), null)) {
                    throw new ValidationException(
                        "The layout contains a seat with the ID " + seatDto.getId()
                            + ", which is not present in the sectors on the top level");
                }
            }
        }
        if (!duplicates.isEmpty()) {
            throw new ValidationException(
                "All seat IDs must be unique. Found non-unique values:" + Arrays.toString(
                    duplicates.toArray()));
        }
    }

    void checkLayoutSeatValid(SeatingPlanSeatDto seat,
        Map<Long, SeatingPlanSectorDto> seatingPlanSectorDtos) {
        if (!Objects.equals(seat.getLocation(), null) && seatingPlanSectorDtos.get(
            seat.getSectorId()).getNoSeats()) {
            throw new ValidationException("Seat " + seat.getId()
                + " is not allowed to have a location, because its sector is a standing sector.");
        }
        if (Objects.equals(seat.getLocation(), null) && !seatingPlanSectorDtos.get(
            seat.getSectorId()).getNoSeats()) {
            throw new ValidationException("Seat " + seat.getId()
                + " must have a location, because its sector is not a standing sector.");
        }
    }

    void checkSectorsValid(List<ProvisionalSectorDto> sectors) {
        Set<Long> existingIds = new HashSet<>();
        List<Long> duplicates = new ArrayList<>();
        for (ProvisionalSectorDto sector : sectors) {
            if (!existingIds.add(sector.getId())) {
                duplicates.add(sector.getId());
            }
        }
        if (!duplicates.isEmpty()) {
            throw new ValidationException(
                "All sector IDs must be unique. Found non-unique values:" + Arrays.toString(
                    duplicates.toArray()));
        }
    }

    void checkSeatsValid(SeatingPlanWithoutIdDto seatingPlanWithoutId) {
        Map<Long, ProvisionalSectorDto> sectorMap = new HashMap<>();
        for (ProvisionalSectorDto sector : seatingPlanWithoutId.getSectors()) {
            sectorMap.put(sector.getId(), sector);
        }
        Set<Long> existingIds = new HashSet<>();
        List<Long> duplicates = new ArrayList<>();
        for (SeatDto seatDto : seatingPlanWithoutId.getSeats()) {
            if (!existingIds.add(seatDto.getId())) {
                duplicates.add(seatDto.getId());
            }
            if (duplicates.isEmpty()) {
                checkSeatValid(seatDto, sectorMap);
            }
        }
        if (!duplicates.isEmpty()) {
            throw new ValidationException(
                "All seat IDs must be unique. Found non-unique values:" + Arrays.toString(
                    duplicates.toArray()));
        }
    }

    void checkSeatValid(SeatDto seat, Map<Long, ProvisionalSectorDto> sectors) {
        ProvisionalSectorDto sector = sectors.get(seat.getSector());
        if (Objects.equals(sector, null)) {
            throw new ValidationException(
                "The Seat with the number " + seat.getId() + " has no valid sector");
        }
    }
}
