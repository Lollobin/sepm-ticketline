package at.ac.tuwien.sepm.groupphase.backend.unittests.Transaction;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.SectorPrice;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.embeddables.BookedInKey;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.BookedInRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorPriceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.OrderServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderServiceTest implements TestData {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AuthenticationUtil authenticationFacade;
    @Mock
    private SectorPriceRepository sectorPriceRepository;
    @Mock
    private BookedInRepository bookedInRepository;
    private OrderService orderService;

    private final List<Transaction> transactionsToReturn = new ArrayList<>();

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(transactionRepository, authenticationFacade, sectorPriceRepository, bookedInRepository);
    }

    @Test
    void whenExistingUser_thenOrdersByUserShouldBeFound() {
        ApplicationUser loggedInUser = new ApplicationUser();
        loggedInUser.setEmail(USER_EMAIL);

        Transaction transaction = new Transaction();
        transaction.setDate(TRANSACTION_DATE);
        transaction.setUser(loggedInUser);

        transactionsToReturn.add(transaction);

        when(transactionRepository.findAllByUserEmailOrderByDateDesc(USER_EMAIL)).thenReturn(transactionsToReturn);
        when(authenticationFacade.getEmail()).thenReturn(USER_EMAIL);

        List<Transaction> found = orderService.findAllByCurrentUser();

        assertAll(
            () -> assertEquals(1, found.size()),
            () -> assertEquals(USER_EMAIL, found.get(0).getUser().getEmail())
        );
    }


    private Ticket generateTicketWithSeatAndSectorAndShow(long id) {
        Seat seat = new Seat();
        seat.setSeatId(id);
        Sector sector = new Sector();
        sector.setSectorId(id);
        seat.setSector(sector);
        Ticket ticket = new Ticket();
        ticket.setTicketId(id);
        ticket.setSeat(seat);
        Show show = new Show();
        show.setShowId(id);
        ticket.setShow(show);
        return ticket;
    }

    private BookedIn generateBookedInFromTicket(Transaction transaction, Ticket ticket, SectorPrice sectorPrice){
        BookedIn bookedIn = new BookedIn();
        BookedInKey bookedInKey = new BookedInKey();
        bookedInKey.setTicketId(ticket.getTicketId());
        bookedIn.setId(bookedInKey);
        bookedIn.setTransaction(transaction);
        bookedIn.setBookingType(BookingType.PURCHASE);
        bookedIn.setPriceAtBookingTime(sectorPrice.getPrice());
        bookedIn.setTicket(ticket);
        return bookedIn;
    }

    @Test
    void testGenerateTransaction_shouldSaveBookedInAndTransaction(){
        ApplicationUser applicationUser = new ApplicationUser();
        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSectorAndShow(1L));
        SectorPrice sectorPrice = new SectorPrice();
        sectorPrice.setPrice(BigDecimal.TEN);
        Transaction transaction = new Transaction();
        transaction.setUser(applicationUser);
        when(sectorPriceRepository.findOneByShowIdBySectorId(1L, 1L)).thenReturn(sectorPrice);
        orderService.generateTransaction(ticketList, applicationUser, BookingType.PURCHASE);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(bookedInRepository).saveAll(Set.of(generateBookedInFromTicket(transaction, ticketList.get(0), sectorPrice)));

    }

    @Test
    void testGenerateTransaction_shouldSaveMultipleBookedInsAndTransaction(){
        ApplicationUser applicationUser = new ApplicationUser();
        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSectorAndShow(1L), generateTicketWithSeatAndSectorAndShow(2L));
        SectorPrice sectorPrice = new SectorPrice();
        sectorPrice.setPrice(BigDecimal.TEN);
        Transaction transaction = new Transaction();
        transaction.setUser(applicationUser);

        when(sectorPriceRepository.findOneByShowIdBySectorId(1L, 1L)).thenReturn(sectorPrice);
        when(sectorPriceRepository.findOneByShowIdBySectorId(2L, 2L)).thenReturn(sectorPrice);

        orderService.generateTransaction(ticketList, applicationUser, BookingType.PURCHASE);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(bookedInRepository).saveAll(Set.of(generateBookedInFromTicket(transaction, ticketList.get(0), sectorPrice), generateBookedInFromTicket(transaction, ticketList.get(1), sectorPrice)));

    }
    @Test
    void testGenerateTransaction_shouldDoNothingWhenTicketListEmpty(){
        ApplicationUser applicationUser = new ApplicationUser();
        List<Ticket> ticketList = List.of();
        orderService.generateTransaction(ticketList, applicationUser, BookingType.PURCHASE);
        verify(transactionRepository, times(0)).save(any(Transaction.class));

    }
}
