package at.ac.tuwien.sepm.groupphase.backend.unittests.TicketCancellation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.CancellationValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class CancellationValidatorTest {

    CancellationValidator cancellationValidator;

    @BeforeEach
    void setUp() {
        cancellationValidator = new CancellationValidator();
    }

    @Test
    void testValidateTicketInformation_shouldAllowValidCancellation() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of());
        ticketDto.setPurchased(List.of(1L));
        cancellationValidator.validateTicketInformation(ticketDto);
    }

    @Test
    void testValidateTicketInformation_shouldAllowValidDeReservation() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of(1L, 2L, 3L));
        ticketDto.setPurchased(List.of());
        cancellationValidator.validateTicketInformation(ticketDto);
    }

    @Test
    void testValidateTicketInformation_shouldThrowValidationExceptionOnCancellationAndDeReservation() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of(1L));
        ticketDto.setPurchased(List.of(1L));
        assertThrows(ValidationException.class, () -> {
            cancellationValidator.validateTicketInformation(ticketDto);
        });
    }

    @Test
    void testValidateTicketInformation_shouldThrowValidationExceptionOnNoValues() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of());
        ticketDto.setPurchased(List.of());
        assertThrows(ValidationException.class, () -> {
            cancellationValidator.validateTicketInformation(ticketDto);
        });
    }
}
