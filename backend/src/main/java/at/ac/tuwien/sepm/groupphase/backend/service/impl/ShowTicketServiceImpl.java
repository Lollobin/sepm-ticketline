package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatWithBookingStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingPlanDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowInformationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatingPlanMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectorMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowTicketService;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShowTicketServiceImpl implements ShowTicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ShowRepository showRepository;
    private final TicketRepository ticketRepository;
    private final SeatingPlanMapper seatingPlanMapper;
    private final SectorMapper sectorMapper;

    public ShowTicketServiceImpl(ShowRepository showRepository, TicketRepository ticketRepository,
        SeatingPlanMapper seatingPlanMapper, SectorMapper sectorMapper) {
        this.showRepository = showRepository;
        this.ticketRepository = ticketRepository;
        this.seatingPlanMapper = seatingPlanMapper;
        this.sectorMapper = sectorMapper;
    }

    @Override
    public ShowInformationDto findOne(Long id) {
        Optional<Show> show = this.showRepository.findById(id);
        if (show.isEmpty()) {
            throw new NotFoundException(String.format("Could not find show with id %s", id));
        }
        List<Ticket> ticketList = this.ticketRepository.findByShowId(show.get().getShowId());
        ShowInformationDto showInformation = new ShowInformationDto();
        List<SeatWithBookingStatusDto> seats = new ArrayList<>();
        HashMap<Long, SectorDto> sectors = new HashMap<>();
        SeatingPlanDto seatingPlanDto = new SeatingPlanDto();
        for (Ticket ticket : ticketList) {
            SeatWithBookingStatusDto element = new SeatWithBookingStatusDto();
            element.setPurchased(ticket.getPurchasedBy() != null);
            element.setReserved(ticket.getReservedBy() != null);
            Seat seat = ticket.getSeat();
            element.setSeatId(seat.getSeatId());
            element.setRowNumber(seat.getRowNumber());
            element.setSeatNumber(seat.getSeatNumber());

            Sector sector = seat.getSector();
            element.setSector(sector.getSectorId());
            sectors.putIfAbsent(sector.getSectorId(), this.sectorMapper.sectorToSectorDto(sector));
            SeatingPlan seatingPlan = sector.getSeatingPlan();

            seatingPlanDto.setSeatingPlanLayoutId(seatingPlan.getSeatingPlanLayout().getSeatingPlanLayoutId());
            seatingPlanDto.setName(seatingPlan.getName());
            seatingPlanDto.setLocationId(seatingPlan.getLocation().getId());
            seatingPlanDto.setSeatingPlanId(seatingPlan.getSeatingPlanId());
            seats.add(element);

        }
        showInformation.setSeats(seats);
        showInformation.setSectors(sectors.values().stream().toList());
        showInformation.setSeatingPlan(seatingPlanDto);
        return showInformation;
    }
}
