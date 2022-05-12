package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import java.lang.invoke.MethodHandles;
import java.util.List;

import at.ac.tuwien.sepm.groupphase.backend.validator.ShowValidator;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ShowServiceImpl implements ShowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ShowRepository showRepository;
    private final ShowValidator showValidator;
    private final SectorRepository sectorRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;

    public ShowServiceImpl(ShowRepository showRepository, ShowValidator showValidator,
        SectorRepository sectorRepository, SeatRepository seatRepository,
        TicketRepository ticketRepository) {
        this.showRepository = showRepository;
        this.showValidator = showValidator;
        this.sectorRepository = sectorRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
    }


    @Override
    public List<Show> findAll() {
        LOGGER.debug("Find all shows");
        return showRepository.findAll();
    }

    @Override
    public Show createShow(Show show, Long seatingPlanId) {
        LOGGER.debug("Create new show {}", show);

        showValidator.checkIfShowCorrect(show);

        List<Sector> sectors = sectorRepository.findAllBySeatingPlanId(seatingPlanId);

        for (Sector sector : sectors){
            List<Seat> seats = seatRepository.findBySectorId(sector.getSectorId());
            for (Seat seat : seats) {
                Ticket ticket = new Ticket();
                ticket.setSeat(seat);
                ticket.setShow(show);
                ticketRepository.save(ticket);
            }
        }

        return showRepository.save(show);
    }

    @Override
    public Show findOne(Long id) {
        Optional<Show> show = showRepository.findById(id);
        Hibernate.initialize(show);
        if (show.isPresent()) {
            return show.get();
        } else {
            throw new NotFoundException(String.format("Could not find show with id %s", id));
        }
    }
}
