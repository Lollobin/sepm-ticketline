package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.BookingTypeDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TransactionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.enums.BookingType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {


    default OffsetDateTime map(LocalDate localDate) {
        return OffsetDateTime.of(localDate, LocalTime.MIDNIGHT, ZoneOffset.UTC);
    }

    TransactionDto transActionToTransactionDto(Transaction transaction);

    List<TransactionDto> transActionToTransactionDto(List<Transaction> transaction);


    BookingTypeDto bookingTypeToBookingTypeDto(BookingType bookingType);


    default OrderDto transActionToOrderDto(Transaction transaction) {
        OrderDto orderDto = new OrderDto();

        orderDto.setType(bookingTypeToBookingTypeDto(
            transaction.getBookedIns().iterator().next().getBookingType()));

        return orderDto;
    }

    List<OrderDto> transActionToOrderDto(List<Transaction> transactions);


}
