package at.ac.tuwien.sepm.groupphase.backend.unittests.InvoicePrinting;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADDRESS_ENTITY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_BANDNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_FIRSTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_KNOWNAS;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTIST_LASTNAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CATEGORY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_CONTENT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_DURATION;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.EVENT_NAME;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEATINGPLANLAYOUT_PATH;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SEAT_ID1;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SECTOR_ID1;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.SHOW_DATE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomAuthenticationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.TransactionPdfService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TransactionPdfServiceImpl;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TransactionPdfServiceTest {

    TransactionPdfService transactionPdfService;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    private AuthenticationUtil authenticationFacade;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        transactionPdfService = new TransactionPdfServiceImpl(transactionRepository,
            authenticationFacade,
            userRepository);
    }


    Set<BookedIn> generateBookedIn(Transaction transaction, Ticket ticket) {
        BookedIn bookedIn = new BookedIn();
        bookedIn.setTransaction(transaction);
        bookedIn.setTicket(ticket);
        bookedIn.setPriceAtBookingTime(BigDecimal.TEN);
        bookedIn.setBookingType(BookingType.PURCHASE);
        return Set.of(bookedIn);

    }

    Ticket generateTicket(ApplicationUser purchasedUser) {
        Ticket ticket = new Ticket();
        ticket.setPurchasedBy(purchasedUser);
        ticket.setShow(generateShow());
        ticket.setSeat(generateSeatStructure());
        return ticket;
    }

    Transaction generateTransaction(ApplicationUser user) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setUser(user);
        transaction.setBookedIns(generateBookedIn(transaction, generateTicket(user)));
        transaction.setDate(OffsetDateTime.of(LocalDateTime.now(), ZoneOffset.UTC));
        return transaction;
    }

    private Seat generateSeatStructure() {
        Event event = new Event();
        event.setCategory(EVENT_CATEGORY);
        event.setContent(EVENT_CONTENT);
        event.setName(EVENT_NAME);
        event.setDuration(EVENT_DURATION);

        Location location = new Location();
        location.setName("Filip's Cool Location");
        ADDRESS_ENTITY.setAddressId(null);
        location.setAddress(ADDRESS_ENTITY);

        SeatingPlanLayout seatingPlanLayout = new SeatingPlanLayout();
        seatingPlanLayout.setSeatingLayoutPath(SEATINGPLANLAYOUT_PATH);

        SeatingPlan seatingPlan = new SeatingPlan();
        seatingPlan.setName("Filip's Corresponding Seating Plan");
        seatingPlan.setLocation(location);
        seatingPlan.setSeatingPlanLayout(seatingPlanLayout);

        Sector sector = new Sector(SECTOR_ID1);
        sector.setSeatingPlan(seatingPlan);

        Seat seat = new Seat();
        seat.setSeatId(SEAT_ID1);
        seat.setSector(sector);
        return seat;


    }

    private Artist generateArtist() {
        Artist artist = new Artist();
        artist.setArtistId(1L);
        artist.setFirstName(ARTIST_FIRSTNAME);
        artist.setLastName(ARTIST_LASTNAME);
        artist.setKnownAs(ARTIST_KNOWNAS);
        artist.setBandName(ARTIST_BANDNAME);
        return artist;
    }

    private Show generateShow() {

        Event event = new Event();
        event.setCategory(EVENT_CATEGORY);
        event.setContent(EVENT_CONTENT);
        event.setName(EVENT_NAME);
        event.setDuration(EVENT_DURATION);

        Show show = new Show();

        show.setEvent(event);
        show.setArtists(Set.of(generateArtist()));
        show.setDate(SHOW_DATE);
        show.setSectorPrices(new HashSet<>());

        return show;
    }

    ApplicationUser generateUser() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setEmail("email");
        applicationUser.setUserId(1L);
        applicationUser.setFirstName("");
        applicationUser.setLastName("");
        Address address = new Address();
        address.setHouseNumber("");
        address.setCountry("");
        address.setCity("");
        address.setZipCode("");
        address.setStreet("");
        applicationUser.setAddress(address);
        return applicationUser;
    }


    @Test
    void getTransactionPdf_shouldCreatePdf() throws IOException {
        ApplicationUser user = generateUser();
        Transaction transaction = generateTransaction(user);

        when(transactionRepository.findById(1L)).thenReturn(Optional.ofNullable(transaction));
        when(userRepository.findUserByEmail("email")).thenReturn(user);
        when(authenticationFacade.getEmail()).thenReturn("email");
        Resource resource = transactionPdfService.getTransactionPdf(1L);
        //We have currently no way to compare PDF outputs, so we check if the generated resource is not null
        assertThat(resource).isNotNull();
    }

    @Test
    void getTransactionPdf_throwsNotFoundWhenTransactionIdDoesNotExist() throws IOException {
        ApplicationUser user = generateUser();
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> transactionPdfService.getTransactionPdf(1L));
    }

    @Test
    void getTransactionPdf_throwsCustomAuthExceptionWhenUserNotAuthorized() throws IOException {

        ApplicationUser user = generateUser();
        Transaction transaction = generateTransaction(user);
        when(transactionRepository.findById(1L)).thenReturn(Optional.ofNullable(transaction));
        when(authenticationFacade.getEmail()).thenReturn("otheremail");
        assertThrows(CustomAuthenticationException.class,
            () -> transactionPdfService.getTransactionPdf(1L));
    }
}
