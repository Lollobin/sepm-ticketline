package at.ac.tuwien.sepm.groupphase.backend.unittests.TicketAcquisition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.FullTicketWithStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
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
import at.ac.tuwien.sepm.groupphase.backend.service.TicketAcquisitionService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.TicketAcquisitionServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.PurchaseValidator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TicketAcquisitionServiceTest {

    @Mock
    private PurchaseValidator purchaseValidator;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    TicketAcquisitionService ticketAcquisitionService;
    @Mock
    private AuthenticationUtil authenticationFacade;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderService orderService;

    @Mock
    private Authentication authentication;
    AtomicLong atomicLong;

    @BeforeEach
    void setUp() {
        ticketAcquisitionService = new TicketAcquisitionServiceImpl(purchaseValidator,
            ticketRepository, authenticationFacade, userRepository, orderService);
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

    private TicketDto mapDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setTicketId(ticket.getTicketId());
        ticketDto.setSector(ticket.getSeat().getSector().getSectorId());
        ticketDto.setSeatNumber(ticket.getSeat().getSeatNumber());
        ticketDto.setRowNumber(ticket.getSeat().getRowNumber());
        return ticketDto;
    }

    @Test
    void acquireTickets_shouldReturnSinglePurchasedTicket() {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        long id = atomicLong.get();

        List<Long> ticketIds = List.of(id);
        ticketStatusDto.setPurchased(ticketIds);

        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSector(id));
        when(ticketRepository.findAllById(ticketIds)).thenReturn(ticketList);

        ApplicationUser user = new ApplicationUser();
        when(authentication.getPrincipal()).thenReturn("");
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByEmail(any())).thenReturn(user);
        when(ticketRepository.saveAllAndFlush(ticketList)).thenReturn(ticketList);

        FullTicketWithStatusDto ticketDto = ticketAcquisitionService.acquireTickets(
            ticketStatusDto);
        assertThat(ticketDto.getPurchased()).containsAll(
            ticketList.stream().map(this::mapDto).toList());
        verify(orderService).generateTransaction(ticketList, user, BookingType.PURCHASE);

    }

    @Test
    void acquireTickets_shouldReturnMultiplePurchasedTickets() {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        long id1 = atomicLong.get();
        long id2 = atomicLong.get();

        List<Long> ticketIds = List.of(id1, id2);
        ticketStatusDto.setPurchased(ticketIds);

        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSector(id1),
            generateTicketWithSeatAndSector(id2));
        when(ticketRepository.findAllById(ticketIds)).thenReturn(ticketList);

        ApplicationUser user = new ApplicationUser();
        when(authentication.getPrincipal()).thenReturn("");

        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        when(ticketRepository.saveAllAndFlush(ticketList)).thenReturn(ticketList);

        FullTicketWithStatusDto ticketDto = ticketAcquisitionService.acquireTickets(
            ticketStatusDto);
        assertThat(ticketDto.getPurchased()).containsAll(
            ticketList.stream().map(this::mapDto).toList());
        verify(orderService).generateTransaction(ticketList, user, BookingType.PURCHASE);
    }

    @Test
    void acquireTickets_shouldReturnSingleReservedTicket() {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        long id = atomicLong.get();

        List<Long> ticketIds = List.of(id);
        ticketStatusDto.setReserved(ticketIds);

        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSector(id));
        when(ticketRepository.findAllById(ticketIds)).thenReturn(ticketList);

        ApplicationUser user = new ApplicationUser();
        when(authentication.getPrincipal()).thenReturn("");
        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByEmail(any())).thenReturn(user);
        when(ticketRepository.saveAllAndFlush(ticketList)).thenReturn(ticketList);

        FullTicketWithStatusDto ticketDto = ticketAcquisitionService.acquireTickets(
            ticketStatusDto);
        assertThat(ticketDto.getReserved()).containsAll(
            ticketList.stream().map(this::mapDto).toList());
        verify(orderService).generateTransaction(ticketList, user, BookingType.RESERVATION);

    }

    @Test
    void acquireTickets_shouldReturnMultipleReservedTickets() {
        TicketStatusDto ticketStatusDto = new TicketStatusDto();
        long id1 = atomicLong.get();
        long id2 = atomicLong.get();

        List<Long> ticketIds = List.of(id1, id2);
        ticketStatusDto.setReserved(ticketIds);

        List<Ticket> ticketList = List.of(generateTicketWithSeatAndSector(id1),
            generateTicketWithSeatAndSector(id2));
        when(ticketRepository.findAllById(ticketIds)).thenReturn(ticketList);

        ApplicationUser user = new ApplicationUser();
        when(authentication.getPrincipal()).thenReturn("");

        when(authenticationFacade.getAuthentication()).thenReturn(authentication);
        when(userRepository.findUserByEmail(any())).thenReturn(user);

        when(ticketRepository.saveAllAndFlush(ticketList)).thenReturn(ticketList);

        FullTicketWithStatusDto ticketDto = ticketAcquisitionService.acquireTickets(
            ticketStatusDto);
        assertThat(ticketDto.getReserved()).containsAll(
            ticketList.stream().map(this::mapDto).toList());
        verify(orderService).generateTransaction(ticketList, user, BookingType.RESERVATION);
    }

}
