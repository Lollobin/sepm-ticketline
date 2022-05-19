package at.ac.tuwien.sepm.groupphase.backend.unittests.TicketAcquisition;

import static org.junit.jupiter.api.Assertions.assertThrows;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketStatusDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.PurchaseValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PurchaseValidatorTest {

    PurchaseValidator purchaseValidator;

    @BeforeEach
    void setUp() {
        purchaseValidator = new PurchaseValidator();
    }

    @Test
    void testValidateTicketInformation_shouldAllowValidPurchase() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of());
        ticketDto.setPurchased(List.of(1L));
        purchaseValidator.validateTicketInformation(ticketDto);
    }

    @Test
    void testValidateTicketInformation_shouldAllowValidReservation() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of(1L, 2L, 3L));
        ticketDto.setPurchased(List.of());
        purchaseValidator.validateTicketInformation(ticketDto);
    }

    @Test
    void testValidateTicketInformation_shouldThrowValidationExceptionOnPurchaseAndReservation() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of(1L));
        ticketDto.setPurchased(List.of(1L));
        assertThrows(ValidationException.class, () -> {
            purchaseValidator.validateTicketInformation(ticketDto);
        });

    }

    @Test
    void testValidateTicketInformation_shouldThrowValidationExceptionOnNoValues() {
        TicketStatusDto ticketDto = new TicketStatusDto();
        ticketDto.setReserved(List.of());
        ticketDto.setPurchased(List.of());
        assertThrows(ValidationException.class, () -> {
            purchaseValidator.validateTicketInformation(ticketDto);
        });
    }
}
