package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
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
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedInRepository;
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
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BookedInRepository bookedInRepository;
    private final TransactionRepository transactionRepository;

    //private ApplicationUser user;

    public ShowDataGenerator(ShowRepository showRepository, ArtistRepository artistRepository,
        EventRepository eventRepository, AddressRepository addressRepository,
        LocationRepository locationRepository,
        SeatingPlanLayoutRepository seatingPlanLayoutRepository,
        SeatingPlanRepository seatingPlanRepository, SectorRepository sectorRepository,
        SeatRepository seatRepository, TicketRepository ticketRepository,
        SectorPriceRepository sectorPriceRepository, FileSystemRepository fileSystemRepository,
        UserRepository userRepository,
        BookedInRepository bookedInRepository,
        TransactionRepository transactionRepository) {
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
        this.userRepository = userRepository;
        this.bookedInRepository = bookedInRepository;
        this.transactionRepository = transactionRepository;
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
    public void generateData() throws IOException {
        if (!showRepository.findAll().isEmpty()) {
            LOGGER.info("shows already generated");
            return;
        }

        Artist a1 = new Artist();
        a1.setArtistId(1L);
        a1.setFirstName("Al");
        a1.setLastName("Yankovic");
        a1.setKnownAs("Weird Al");
        artistRepository.save(a1);

        Event e1 = new Event();
        e1.setEventId(1L);
        e1.setDuration(120);
        e1.setCategory("POP");
        e1.setName("Weird Al's cool tour");
        eventRepository.save(e1);

        Show show = new Show();
        show.setShowId(1L);
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


        Address address2 = new Address();
        address2.setStreet("TestStreet 1233");
        address2.setZipCode("219338");
        address2.setCity("test3City");
        address2.setCountry("Aust3ria");
        address2.setHouseNumber("2");

        ApplicationUser user = new ApplicationUser();
        user.setEmail("admin@email.com");
        user.setFirstName("Admin");
        user.setLastName("User");
        user.setGender(Gender.FEMALE);
        user.setAddress(address2);
        user.setPassword("password");
        user.setHasAdministrativeRights(true);
        user.setLoginTries(0);
        user.setMustResetPassword(false);
        user.setLockedAccount(false);
        userRepository.save(user);

        for (int i = 0; i < 5; i++) {
            Sector sector = generateSector(seatingPlan);
            sectorRepository.save(sector);
            SectorPrice sectorPrice = generateSectorPrice(sector, show);
            sectorPriceRepository.save(sectorPrice);
            for (int j = 0; j < 5; j++) {
                Seat seat = generateSeat(sector);
                seatRepository.save(seat);
                Ticket ticket = generateTicket(show, seat);
                ticket.setPurchasedBy(user);
                ticketRepository.save(ticket);
            }
        }
        //generateTransactions();
    }

    private SectorPrice generateSectorPrice(Sector sector, Show show) {
        SectorPrice sectorPrice = new SectorPrice();
        sectorPrice.setId(new SectorPriceId(sector.getSectorId(), show.getShowId()));
        sectorPrice.setSector(sector);
        sectorPrice.setShow(show);
        sectorPrice.setPrice(BigDecimal.valueOf((Math.random() + 1) * 255));
        return sectorPrice;
    }

    /*
    // TODO: move to separate class
    public void generateTransactions() {
        if (!transactionRepository.findAll().isEmpty()) {
            LOGGER.debug("order already generated");
        } else {
            Address address2 = new Address();
            address2.setStreet("TestStreet 1233");
            address2.setZipCode("219338");
            address2.setCity("test3City");
            address2.setCountry("Aust3ria");
            address2.setHouseNumber("2");

            addressRepository.save(address2);

            ApplicationUser user2 = new ApplicationUser();
            user2.setEmail("user@email.com");
            user2.setFirstName("Admin");
            user2.setLastName("User");
            user2.setGender(Gender.MALE);
            user2.setAddress(address2);
            user2.setPassword("password");
            user2.setHasAdministrativeRights(true);
            user2.setLoginTries(0);
            user2.setMustResetPassword(false);
            user2.setLockedAccount(false);
            userRepository.save(user2);

            final Ticket ticket1 = ticketRepository.getById(1L);
            Ticket ticket2 = ticketRepository.getById(2L);

            //Buy Ticket 1
            Transaction transaction1 = new Transaction();
            transaction1.setDate(OffsetDateTime.of(2005, 11, 20, 0, 0, 0, 0, ZoneOffset.UTC));
            transaction1.setUser(user);
            transactionRepository.save(transaction1);

            BookedIn bookedIn1 = new BookedIn();
            bookedIn1.setBookingType(BookingType.PURCHASE);
            bookedIn1.setTransaction(transaction1);
            bookedIn1.setPriceAtBookingTime(22.3f);
            bookedIn1.setTicket(ticket1);
            bookedInRepository.save(bookedIn1);

            //Cancel Ticket 1
            Transaction transaction2 = new Transaction();
            transaction2.setDate(OffsetDateTime.of(2005, 11, 30, 0, 0, 0, 0, ZoneOffset.UTC));
            transaction2.setUser(user);
            transactionRepository.save(transaction2);

            BookedIn bookedIn2 = new BookedIn();
            bookedIn2.setBookingType(BookingType.CANCELLATION);
            bookedIn2.setTransaction(transaction2);
            bookedIn2.setPriceAtBookingTime(22.3f);
            bookedIn2.setTicket(ticket1);
            bookedInRepository.save(bookedIn2);

            //Reserve Ticket 2
            Transaction transaction3 = new Transaction();
            transaction3.setDate(OffsetDateTime.of(2006, 3, 1, 0, 0, 0, 0, ZoneOffset.UTC));
            transaction3.setUser(user);
            transactionRepository.save(transaction3);

            BookedIn bookedIn3 = new BookedIn();
            bookedIn3.setBookingType(BookingType.RESERVATION);
            bookedIn3.setTransaction(transaction3);
            bookedIn3.setPriceAtBookingTime(22.3f);
            bookedIn3.setTicket(ticket2);
            bookedInRepository.save(bookedIn3);

        }


    }

     */
}
