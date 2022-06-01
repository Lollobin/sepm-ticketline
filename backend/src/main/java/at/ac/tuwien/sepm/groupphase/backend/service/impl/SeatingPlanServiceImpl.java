package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ProvisionalSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanLayoutDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatingPlanMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingPlanService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.SeatingPlanValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SeatingPlanServiceImpl implements SeatingPlanService {

    private final SeatingPlanValidator seatingPlanValidator;
    private final SeatingPlanRepository seatingPlanRepository;
    private final SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    private final FileSystemRepository fileSystemRepository;
    private final SeatingPlanMapper seatingPlanMapper;
    private final SectorRepository sectorRepository;
    private final SeatMapper seatMapper;
    private final SeatRepository seatRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SeatingPlanServiceImpl(SeatingPlanValidator seatingPlanValidator,
        SeatingPlanRepository seatingPlanRepository,
        SeatingPlanLayoutRepository seatingPlanLayoutRepository,
        FileSystemRepository fileSystemRepository, SeatingPlanMapper seatingPlanMapper,
        SectorRepository sectorRepository, SeatMapper seatMapper, SeatRepository seatRepository) {
        this.seatingPlanValidator = seatingPlanValidator;
        this.seatingPlanRepository = seatingPlanRepository;
        this.seatingPlanLayoutRepository = seatingPlanLayoutRepository;
        this.fileSystemRepository = fileSystemRepository;
        this.seatingPlanMapper = seatingPlanMapper;
        this.sectorRepository = sectorRepository;
        this.seatMapper = seatMapper;
        this.seatRepository = seatRepository;
    }

    @Override
    public Long createSeatingPlan(SeatingPlanWithoutIdDto seatingPlanWithoutIdDto) {
        seatingPlanValidator.checkSeatingPlanValid(seatingPlanWithoutIdDto);
        SeatingPlanLayout savedSeatingPlanLayout = saveSeatingPlanLayout(
            seatingPlanWithoutIdDto.getSeatingPlanLayout(), seatingPlanWithoutIdDto.getName(), new SeatingPlanLayout());
        SeatingPlan seatingPlan = saveSeatingPlan(seatingPlanWithoutIdDto, savedSeatingPlanLayout);
        Map<Long, Sector> provisionalIdSectorMapping = new HashMap<>();
        for (ProvisionalSectorDto provisionalSectorDto : seatingPlanWithoutIdDto.getSectors()) {
            Sector sector = new Sector();
            sector.setSeatingPlan(seatingPlan);
            Sector savedSector = sectorRepository.save(sector);
            provisionalIdSectorMapping.put(provisionalSectorDto.getId(), savedSector);
        }
        Map<Long, Seat> provisionalIdSeatMapping = new HashMap<>();
        for (SeatDto seatDto : seatingPlanWithoutIdDto.getSeats()) {
            Seat seat = this.seatMapper.seatDtoToSeat(seatDto);
            seat.setSector(provisionalIdSectorMapping.get(seat.getSector().getSectorId()));
            provisionalIdSeatMapping.put(seatDto.getId(), this.seatRepository.save(seat));
        }

        SeatingPlanLayoutDto updatedSeatingPlanLayout = updateSeatingPlanLayoutIds(
            seatingPlanWithoutIdDto.getSeatingPlanLayout(),
            provisionalIdSectorMapping, provisionalIdSeatMapping);

        saveSeatingPlanLayout(updatedSeatingPlanLayout, seatingPlanWithoutIdDto.getName(), savedSeatingPlanLayout);
        return seatingPlan.getSeatingPlanId();
    }

    private SeatingPlanLayoutDto updateSeatingPlanLayoutIds(
        SeatingPlanLayoutDto provisionalSeatingPlanLayout,
        Map<Long, Sector> provisionalIdSectorMapping,
        Map<Long, Seat> provisionalIdSeatMapping) {
        List<SeatingPlanSeatDto> updatedSeats = new ArrayList<>();
        for (SeatingPlanSeatDto provisionalSeat : provisionalSeatingPlanLayout.getSeats()) {
            provisionalSeat.setSectorId(
                provisionalIdSectorMapping.get(provisionalSeat.getSectorId()).getSectorId());
            provisionalSeat.setId(
                provisionalIdSeatMapping.get(provisionalSeat.getId()).getSeatId());
            updatedSeats.add(provisionalSeat);
        }
        provisionalSeatingPlanLayout.setSeats(updatedSeats);
        List<SeatingPlanSectorDto> updatedSectors = new ArrayList<>();
        for (SeatingPlanSectorDto provisionalSector : provisionalSeatingPlanLayout.getSectors()) {
            provisionalSector.setId(
                provisionalIdSectorMapping.get(provisionalSector.getId()).getSectorId());
            updatedSectors.add(provisionalSector);
        }
        provisionalSeatingPlanLayout.setSectors(updatedSectors);
        return provisionalSeatingPlanLayout;
    }

    private SeatingPlan saveSeatingPlan(SeatingPlanWithoutIdDto seatingPlanWithoutIdDto,
        SeatingPlanLayout seatingPlanLayout) {
        SeatingPlan seatingPlan = seatingPlanMapper.seatingPlanWithoutIdDtoToSeatingPlan(
            seatingPlanWithoutIdDto);
        seatingPlan.setSeatingPlanLayout(seatingPlanLayout);
        return seatingPlanRepository.save(seatingPlan);
    }

    private SeatingPlanLayout saveSeatingPlanLayout(SeatingPlanLayoutDto seatingPlanLayoutDto,
        String seatingPlanName, SeatingPlanLayout seatingPlanLayout) {
        try {
            String fileName = fileSystemRepository.save(
                objectMapper.writeValueAsBytes(seatingPlanLayoutDto), seatingPlanName);
            seatingPlanLayout.setSeatingLayoutPath(fileName);
            return seatingPlanLayoutRepository.save(seatingPlanLayout);
        } catch (IOException e) {
            throw new ValidationException("Something seems to be wrong with the given layout");
        }
    }
}
