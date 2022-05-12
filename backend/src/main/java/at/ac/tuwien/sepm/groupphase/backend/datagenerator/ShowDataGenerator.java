package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.SectorPriceId;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanLayoutRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingPlanRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

//TODO: Replace with proper dataGenerator class and create dataGenerator for users and addresses
@Profile("generateData")
@Component
public class ShowDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    private final ShowRepository showRepository;
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;
    private final SeatingPlanLayoutRepository seatingPlanLayoutRepository;
    private final SeatingPlanRepository seatingPlanRepository;
    private final SectorRepository sectorRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final SectorPriceRepository sectorPriceRepository;
    private final FileSystemRepository fileSystemRepository;

    public ShowDataGenerator(ShowRepository showRepository, ArtistRepository artistRepository,
        EventRepository eventRepository, AddressRepository addressRepository,
        LocationRepository locationRepository,
        SeatingPlanLayoutRepository seatingPlanLayoutRepository,
        SeatingPlanRepository seatingPlanRepository, SectorRepository sectorRepository,
        SeatRepository seatRepository, TicketRepository ticketRepository,
        SectorPriceRepository sectorPriceRepository, FileSystemRepository fileSystemRepository) {
        this.showRepository = showRepository;
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.addressRepository = addressRepository;
        this.locationRepository = locationRepository;
        this.seatingPlanLayoutRepository = seatingPlanLayoutRepository;
        this.seatingPlanRepository = seatingPlanRepository;
        this.sectorRepository = sectorRepository;
        this.seatRepository = seatRepository;
        this.ticketRepository = ticketRepository;
        this.sectorPriceRepository = sectorPriceRepository;
        this.fileSystemRepository = fileSystemRepository;
    }

    private Ticket generateTicket(Show show, Seat seat) {
        Ticket ticket = new Ticket();
        ticket.setShow(show);
        ticket.setSeat(seat);
        return ticket;
    }

    private Seat generateSeat(Sector sector) {
        Seat seat = new Seat();
        seat.setSector(sector);
        seat.setSeatNumber((long) Math.floor(Math.random() * 10));
        seat.setRowNumber((long) Math.floor(Math.random() * 10));
        return seat;
    }

    private Sector generateSector(SeatingPlan seatingPlan) {
        Sector sector = new Sector();
        sector.setSeatingPlan(seatingPlan);
        return sector;
    }

    private SeatingPlan generateSeatingPlan(Location location,
        SeatingPlanLayout seatingPlanLayout) {
        SeatingPlan seatingPlan = new SeatingPlan();
        seatingPlan.setName("Hall A");
        seatingPlan.setLocation(location);
        seatingPlan.setSeatingPlanLayout(seatingPlanLayout);
        return seatingPlan;
    }

    private SeatingPlanLayout generateSeatingPlanLayout() throws IOException {
        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        FileInputStream fis = new FileInputStream(ResourceUtils.getFile(
                "src/main/java/at/ac/tuwien/sepm/groupphase/backend/datagenerator/seatingPlan1.json")
            .getAbsoluteFile());
        String path = this.fileSystemRepository.save(
            fis.readAllBytes(), "test");
        fis.close();
        seatingPlanLayout.setSeatingLayoutPath(path);
        return seatingPlanLayout;
    }

    private Location generateLocation(Address address) {
        Location location = new Location();
        location.setName("MARX Halle");
        location.setAddress(address);
        return location;
    }

    private Address generateAddress() {
        Address address = new Address();
        address.setStreet("TestStreet 123");
        address.setZipCode("21938");
        address.setCity("testCity");
        address.setCountry("Austria");
        address.setHouseNumber("3");
        return address;
    }

    @PostConstruct
    private void generateData() throws IOException {
        if (!showRepository.findAll().isEmpty()) {
            LOGGER.info("shows already generated");
            return;
        }
        Artist a1 = new Artist();
        a1.setArtistId(Long.valueOf(1));
        a1.setFirstName("Al");
        a1.setLastName("Yankovic");
        a1.setKnownAs("Weird Al");
        artistRepository.save(a1);

        Event e1 = new Event();
        e1.setEventId(Long.valueOf(1));
        e1.setDuration(120);
        e1.setCategory("POP");
        e1.setName("Weird Al's cool tour");
        eventRepository.save(e1);

        Show show = new Show();
        show.setShowId(Long.valueOf(1));
        show.setArtists(Set.of(a1));
        show.setDate(OffsetDateTime.now());
        show.setEvent(e1);

        showRepository.save(show);

        Address address = generateAddress();
        addressRepository.save(address);

        Location location = generateLocation(address);
        locationRepository.save(location);

        SeatingPlanLayout seatingPlanLayout = generateSeatingPlanLayout();
        seatingPlanLayoutRepository.save(seatingPlanLayout);

        SeatingPlan seatingPlan = generateSeatingPlan(location, seatingPlanLayout);
        seatingPlanRepository.save(seatingPlan);

        for (int i = 0; i < 5; i++) {
            Sector sector = generateSector(seatingPlan);
            sectorRepository.save(sector);
            SectorPrice sectorPrice = generateSectorPrice(sector, show);
            sectorPriceRepository.save(sectorPrice);
            for (int j = 0; j < 5; j++) {
                Seat seat = generateSeat(sector);
                seatRepository.save(seat);
                Ticket ticket = generateTicket(show, seat);
                ticketRepository.save(ticket);
            }
        }
    }

    private SectorPrice generateSectorPrice(Sector sector, Show show) {
        SectorPrice sectorPrice = new SectorPrice();
        sectorPrice.setId(new SectorPriceId(sector.getSectorId(), show.getShowId()));
        sectorPrice.setSector(sector);
        sectorPrice.setShow(show);
        sectorPrice.setPrice(BigDecimal.valueOf((Math.random() + 1) * 255));
        return sectorPrice;
    }
}
