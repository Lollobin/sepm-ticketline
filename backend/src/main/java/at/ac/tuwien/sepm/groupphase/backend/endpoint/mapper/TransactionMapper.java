package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TransactionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.BookedIn;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {

    TransactionDto transActionToTransactionDto(Transaction transaction);

    List<TransactionDto> transActionToTransactionDto(List<Transaction> transaction);

    BookingTypeDto bookingTypeToBookingTypeDto(BookingType bookingType);

    default OrderDto transActionToOrderDto(Transaction transaction) {
        OrderDto orderDto = new OrderDto();

        orderDto.setTransactionDate(transaction.getDate());
        orderDto.setTransactionId(Math.toIntExact(transaction.getTransactionId()));

        // Usually every correct transaction has bookedIns. This is a workaround to make creating test cases easier
        if (!transaction.getBookedIns().isEmpty()) {
            BookedIn bookedIn = transaction.getBookedIns().iterator().next();
            Ticket ticket = bookedIn.getTicket();
            Show show = ticket.getShow();
            Event event = show.getEvent();
            Location location = ticket.getSeat().getSector().getSeatingPlan().getLocation();

            orderDto.setType(bookingTypeToBookingTypeDto(bookedIn.getBookingType()));
            orderDto.setShowDate(show.getDate());
            orderDto.setArtists(show.getArtists().stream().map(Artist::getKnownAs).toList());
            orderDto.setEventName(event.getName());
            orderDto.setCity(location.getAddress().getCity());
            orderDto.setLocationName(location.getName());
            orderDto.setTicketIds(
                transaction.getBookedIns().stream().map(BookedIn::getTicket)
                    .map(Ticket::getTicketId)
                    .map(Math::toIntExact).toList());
        }

        return orderDto;
    }

    List<OrderDto> transActionToOrderDto(List<Transaction> transactions);
}
