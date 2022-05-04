package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TransactionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import org.mapstruct.Mapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper
public interface TransactionMapper {


    default OffsetDateTime map(LocalDate localDate) {
        return OffsetDateTime.of(localDate, LocalTime.MIDNIGHT, ZoneOffset.UTC);
    }

    TransactionDto transActionToTransactionDto(Transaction transaction);

    List<TransactionDto> transActionToTransactionDto(List<Transaction> transaction);


}
