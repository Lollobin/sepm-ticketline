package at.ac.tuwien.sepm.groupphase.backend.unittests.TicketCancellation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationUtil;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketCancellationService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TicketCancellationServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.CancellationValidator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TicketCancellationServiceTest {

    @Mock
    private CancellationValidator cancellationValidator;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    TicketCancellationService ticketCancellationService;
    @Mock
    private AuthenticationUtil authenticationFacade;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderService orderService;

    AtomicLong atomicLong;

    @BeforeEach
    void setUp() {
        ticketCancellationService = new TicketCancellationServiceImpl(cancellationValidator,
            ticketRepository, userRepository, authenticationFacade, orderService);
        atomicLong = new AtomicLong();
    }

    private Ticket generateTicketWithSeatAndSector(long id) {
        Seat seat = new Seat();
        seat.setSeatId(id);
        Sector sector = new Sector();
        sector.setSectorId(id);
        seat.setSector(sector);
        Ticket ticket = new Ticket();
        ticket.setTicketId(id);
        ticket.setSeat(seat);
        return ticket;
    }

    @Test
    void cancelTickets_shouldReturnSinglePurchasedId() {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        long id = atomicLong.get();

        List<Long> ticketIds = List.of(id);
        ticketStatusDto.setPurchased(ticketIds);
        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSector(id));
        when(ticketRepository.findAllById(ticketIds)).thenReturn(ticketList);

        ApplicationUser user = new ApplicationUser();
        when(authenticationFacade.getEmail()).thenReturn("");
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        TicketStatusDto ticketDto = ticketCancellationService.cancelTickets(ticketStatusDto);
        assertThat(ticketDto.getPurchased()).containsAll(
            ticketList.stream().map(Ticket::getTicketId).toList());
        verify(orderService).generateTransaction(ticketList, user, BookingType.CANCELLATION);
    }

    @Test
    void cancelTickets_shouldReturnMultipleReservedIds() {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        long id1 = 1L;
        long id2 = 2L;

        List<Long> ticketIds = List.of(id1, id2);
        ticketStatusDto.setReserved(ticketIds);
        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSector(id1),
            generateTicketWithSeatAndSector(id2));
        when(ticketRepository.findAllById(ticketIds)).thenReturn(ticketList);

        ApplicationUser user = new ApplicationUser();
        when(authenticationFacade.getEmail()).thenReturn("");
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        when(ticketRepository.saveAllAndFlush(ticketList)).thenReturn(ticketList);

        TicketStatusDto ticketDto = ticketCancellationService.cancelTickets(ticketStatusDto);
        assertThat(ticketDto.getReserved()).containsAll(
            ticketList.stream().map(Ticket::getTicketId).toList());
        verify(orderService).generateTransaction(ticketList, user, BookingType.DERESERVATION);
    }
}
