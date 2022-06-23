package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import com.github.javafaker.Faker;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("generateData")
@Component
public class TicketSectorPriceGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final SeatingPlanRepository seatingPlanRepository;
    private final SectorRepository sectorRepository;
    private final SectorPriceRepository sectorPriceRepository;
    private final Faker faker = new Faker();

    public TicketSectorPriceGenerator(
        TicketRepository ticketRepository,
        SeatRepository seatRepository,
        ShowRepository showRepository,
        SeatingPlanRepository seatingPlanRepository,
        SectorRepository sectorRepository,
        SectorPriceRepository sectorPriceRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
        this.seatingPlanRepository = seatingPlanRepository;
        this.sectorRepository = sectorRepository;
        this.sectorPriceRepository = sectorPriceRepository;
    }

    public void generateData() {
        if (!ticketRepository.findAll().isEmpty()) {
            LOGGER.debug("tickets already generated");
            return;
        }

        LOGGER.debug("generating tickets and sector prices for each show");

        Map<Long, SeatingPlan> seatingPlanMap = new HashMap<>();
        for (SeatingPlan seatingPlan : seatingPlanRepository.findAll()) {
            seatingPlanMap.put(seatingPlan.getSeatingPlanId(), seatingPlan);
        }

        Map<Long, List<Sector>> sectorMap = new HashMap<>();
        for (Sector sector : sectorRepository.findAll()) {
            long id = sector.getSeatingPlan().getSeatingPlanId();
            sectorMap.computeIfAbsent(id, k -> new ArrayList<>());
            sectorMap.get(id).add(sector);
        }

        Map<Long, List<Seat>> seatMap = new HashMap<>();
        for (Seat seat : seatRepository.findAll()) {
            long id = seat.getSector().getSectorId();
            seatMap.computeIfAbsent(id, k -> new ArrayList<>());
            seatMap.get(id).add(seat);
        }

        List<Show> shows = showRepository.findAll();
        int totalNumberOfSeatingPlans = seatingPlanRepository.findAll().size();
        List<SectorPrice> sectorPrices = new ArrayList<>();
        List<Ticket> tickets = new ArrayList<>();
        for (Show show : shows) {
            SeatingPlan randSeatingPlan = seatingPlanMap.get(
                (long) faker.number().numberBetween(1, totalNumberOfSeatingPlans + 1));

            List<Sector> sectors = sectorMap.get(randSeatingPlan.getSeatingPlanId());

            for (Sector sector : sectors) {
                //generate sector price
                SectorPrice sectorPrice = generateSectorPrice(show, sector);
                sectorPrices.add(sectorPrice);

                //generate tickets
                List<Seat> seats = seatMap.get(sector.getSectorId());
                for (Seat seat : seats) {
                    Ticket ticket = generateTicket(seat, show);
                    tickets.add(ticket);
                }
            }

        }
        ticketRepository.saveAll(tickets);
        sectorPriceRepository.saveAll(sectorPrices);
    }

    private SectorPrice generateSectorPrice(Show show, Sector sector) {
        SectorPrice sectorPrice = new SectorPrice();
        sectorPrice.setShow(show);
        sectorPrice.setSector(sector);
        double price = faker.number().randomDouble(2, 5L, 200L);
        sectorPrice.setPrice(BigDecimal.valueOf(price));
        return sectorPrice;
    }

    private Ticket generateTicket(Seat seat, Show show) {
        Ticket ticket = new Ticket();
        ticket.setSeat(seat);
        ticket.setShow(show);
        return ticket;
    }
}
