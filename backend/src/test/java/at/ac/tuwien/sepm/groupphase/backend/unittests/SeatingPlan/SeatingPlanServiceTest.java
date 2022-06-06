package at.ac.tuwien.sepm.groupphase.backend.unittests.SeatingPlan;

import static org.junit.jupiter.api.Assertions.assertEquals;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ElementLocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ProvisionalSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanLayoutDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanLayoutGeneralDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatingPlanMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingPlanService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SeatingPlanServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.SeatingPlanValidator;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SeatingPlanServiceTest {

    SeatingPlanService seatingPlanService;
    private SeatingPlanValidator seatingPlanValidator;
    @Mock
    private SeatingPlanRepository seatingPlanRepository;
    @Mock
    private SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    @Mock
    private FileSystemRepository fileSystemRepository;
    private SeatingPlanMapper seatingPlanMapper;
    @Mock
    private SectorRepository sectorRepository;
    private SeatMapper seatMapper;
    @Mock
    private SeatRepository seatRepository;

    @BeforeEach
    void setUp() {
        seatingPlanValidator = new SeatingPlanValidator();
        seatingPlanMapper = Mappers.getMapper(SeatingPlanMapper.class);
        seatMapper = Mappers.getMapper(SeatMapper.class);

        seatingPlanService = new SeatingPlanServiceImpl(seatingPlanValidator, seatingPlanRepository,
            seatingPlanLayoutRepository, fileSystemRepository, seatingPlanMapper, sectorRepository,
            seatMapper, seatRepository);
    }

    @Test
    void createSeatingPlan_shouldCreateSeatingPlanWithValidId() throws IOException {
        AtomicLong counter = new AtomicLong();
        String pathName = "anyPAth";
        Long seatingPlanId = 1L;
        SeatingPlanWithoutIdDto seatingPlanDto = generateSeatingPlanWithoutIdDto(5,5, 1);
        SeatingPlan seatingPlan = new SeatingPlan();
        seatingPlan.setSeatingPlanId(seatingPlanId);
        seatingPlan.setName(seatingPlanDto.getName());

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        Mockito.when(seatingPlanRepository.save(Mockito.any())).thenReturn(seatingPlan);
        Mockito.when(seatingPlanLayoutRepository.save(Mockito.any())).thenReturn(seatingPlanLayout);
        Mockito.when(seatRepository.save(Mockito.any(Seat.class))).thenAnswer(i -> {
            Seat seat = (Seat) i.getArguments()[0];
            seat.setSeatId(counter.get());
            return seat;
        });
        Mockito.when(sectorRepository.save(Mockito.any(Sector.class))).thenAnswer(i -> {
            Sector sector = (Sector) i.getArguments()[0];
            sector.setSectorId(counter.get());
            return sector;
        });
        Mockito.when(fileSystemRepository.save(Mockito.any(), Mockito.any())).thenReturn(pathName);

        Long result = this.seatingPlanService.createSeatingPlan(seatingPlanDto);
        assertEquals(result, seatingPlanId);
    }

    private static SeatingPlanSectorDto generateSeatingPlanSector(long id, boolean noSeats) {
        SeatingPlanSectorDto sector = new SeatingPlanSectorDto();
        sector.setId(id);
        sector.setNoSeats(noSeats);
        sector.setColor(0L);
        sector.setDescription("Test");
        if (noSeats) {
            ElementLocationDto location = new ElementLocationDto();
            location.setX(BigDecimal.TEN);
            location.setY(BigDecimal.TEN);
            location.setW(BigDecimal.TEN);
            location.setH(BigDecimal.TEN);

            sector.setLocation(location);
        }
        return sector;
    }

    private static SeatingPlanSeatDto generateSeatingPlanSeat(long id, long sector,
        boolean hasLocation) {
        SeatingPlanSeatDto seat = new SeatingPlanSeatDto();
        seat.setId(id);
        seat.setSectorId(sector);
        if (hasLocation) {
            ElementLocationDto location = new ElementLocationDto();
            location.setX(BigDecimal.TEN);
            location.setY(BigDecimal.TEN);
            location.setW(BigDecimal.TEN);
            location.setH(BigDecimal.TEN);
            seat.setLocation(location);
        }
        return seat;
    }

    public static SeatingPlanWithoutIdDto generateSeatingPlanWithoutIdDto(long numberOfSectors,
        long numberOfSeatsPerSector, long locationId) {
        List<SeatingPlanSectorDto> seatingPlanSectors = new ArrayList<>();
        List<SeatingPlanSeatDto> seatingPlanSeats = new ArrayList<>();
        for (long count = 0; count < numberOfSectors; count++) {
            boolean noSeatsSector = count % 2 == 0;
            seatingPlanSectors.add(generateSeatingPlanSector(count, noSeatsSector));
            for (long seatCount = 0; seatCount < numberOfSeatsPerSector; seatCount++) {
                seatingPlanSeats.add(
                    generateSeatingPlanSeat(numberOfSeatsPerSector * count + seatCount, count,
                        !noSeatsSector));
            }
        }
        SeatingPlanLayoutDto seatingPlanLayoutDto = new SeatingPlanLayoutDto();
        seatingPlanLayoutDto.setSectors(seatingPlanSectors);
        seatingPlanLayoutDto.setSeats(seatingPlanSeats);
        seatingPlanLayoutDto.setStaticElements(new ArrayList<>());
        SeatingPlanLayoutGeneralDto generalDto = new SeatingPlanLayoutGeneralDto();
        generalDto.setHeight(BigDecimal.valueOf(100));
        generalDto.setWidth(BigDecimal.valueOf(100));
        seatingPlanLayoutDto.setGeneral(generalDto);

        SeatingPlanWithoutIdDto seatingPlanWithoutIdDto = new SeatingPlanWithoutIdDto();
        seatingPlanWithoutIdDto.setSeatingPlanLayout(seatingPlanLayoutDto);
        seatingPlanWithoutIdDto.setLocationId(locationId);
        seatingPlanWithoutIdDto.setName("Test");
        seatingPlanWithoutIdDto.setSectors(seatingPlanSectors.stream().map(sector -> {
            ProvisionalSectorDto sectorDto = new ProvisionalSectorDto();
            sectorDto.setId(sector.getId());
            return sectorDto;
        }).collect(Collectors.toList()));
        seatingPlanWithoutIdDto.setSeats(seatingPlanSeats.stream().map(seat -> {
            SeatDto seatDto = new SeatDto();
            seatDto.setId(seat.getId());
            seatDto.setSector(seat.getSectorId());
            seatDto.setSeatNumber(0L);
            seatDto.setRowNumber(0L);
            return seatDto;
        }).collect(Collectors.toList()));
        return seatingPlanWithoutIdDto;
    }
}
