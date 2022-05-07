package at.ac.tuwien.sepm.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.assertThrows;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.AddressValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AddressValidationTest implements TestData {

    private AddressValidator addressValidator;

    private AddressDto addressToValidate;

    @BeforeEach
    void setUp() {
        addressValidator = new AddressValidator();
        addressToValidate =
            new AddressDto()
                .houseNumber(USER_HOUSE_NO)
                .street(USER_STREET)
                .city(USER_CITY)
                .country(USER_CTRY)
                .zipCode(USER_ZIPCODE);
    }

    @Test
    void shouldThrowValidationException_InvalidHouseNumber() {
        addressToValidate.setHouseNumber("");
        assertThrows(
            ValidationException.class, () -> addressValidator.validateAddress(addressToValidate));
    }

    @Test
    void shouldThrowValidationException_InvalidCountry() {
        addressToValidate.setCountry("");
        assertThrows(
            ValidationException.class, () -> addressValidator.validateAddress(addressToValidate));
        addressToValidate.setCountry("678");
        addressToValidate.setCountry("");
        assertThrows(
            ValidationException.class, () -> addressValidator.validateAddress(addressToValidate));
    }

    @Test
    void shouldThrowValidationException_InvalidStreet() {
        addressToValidate.setStreet("");
        assertThrows(
            ValidationException.class, () -> addressValidator.validateAddress(addressToValidate));
    }

    @Test
    void shouldThrowValidationException_InvalidZipCode() {
        addressToValidate.setZipCode("");
        assertThrows(
            ValidationException.class, () -> addressValidator.validateAddress(addressToValidate));
    }
}
