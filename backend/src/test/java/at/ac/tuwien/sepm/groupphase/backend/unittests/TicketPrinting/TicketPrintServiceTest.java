package at.ac.tuwien.sepm.groupphase.backend.unittests.TicketPrinting;

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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketIdArrayDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlan;
import at.ac.tuwien.sepm.groupphase.backend.entity.SeatingPlanLayout;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketPrintService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TicketPrintServiceImpl;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TicketPrintServiceTest {

    TicketPrintService ticketPrintService;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    private AuthenticationUtil authenticationFacade;
    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        ticketPrintService = new TicketPrintServiceImpl(ticketRepository, authenticationFacade,
            userRepository);
    }

    Ticket generateTicket(ApplicationUser purchasedUser) {
        Ticket ticket = new Ticket();
        ticket.setPurchasedBy(purchasedUser);
        ticket.setShow(generateShow());
        ticket.setSeat(generateSeatStructure());
        return ticket;
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
        applicationUser.setEmail("");
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
    void getTicketPdf_shouldCreatePdf() throws IOException {
        ApplicationUser user = generateUser();
        when(ticketRepository.findById(1L)).thenReturn(Optional.ofNullable(generateTicket(user)));
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);
        when(authentication.getPrincipal()).thenReturn("");
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        Resource resource = ticketPrintService.getTicketPdf(List.of(1L));
        //We have currently no way to compare PDF outputs, so we check if the generated resource is not null
        assertThat(resource).isNotNull();
    }

    @Test
    void getTicketPdf_throwsNotFoundWhenTicketIdDoesNotExist() throws IOException {
        ApplicationUser user = generateUser();
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        TicketIdArrayDto dto= new TicketIdArrayDto().ticketIds(List.of(1L));
        assertThrows(NotFoundException.class, () -> ticketPrintService.getTicketPdf(List.of(1L)));
    }
}
