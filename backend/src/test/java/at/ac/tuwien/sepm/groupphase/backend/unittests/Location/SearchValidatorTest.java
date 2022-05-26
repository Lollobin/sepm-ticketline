package at.ac.tuwien.sepm.groupphase.backend.unittests.Location;

import static org.junit.jupiter.api.Assertions.assertThrows;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.SearchValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchValidatorTest {

    SearchValidator searchValidator;

    @BeforeEach
    void setUp() {
        searchValidator = new SearchValidator();
    }

    @Test
    void validateLocationSearchDto_shouldThrowExceptionDueToStringLength() {

        String invalidName = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestejfk";

        LocationSearchDto searchDto = new LocationSearchDto();
        searchDto.setName(invalidName);

        assertThrows(ValidationException.class,
            () -> searchValidator.validateLocationSearchDto(searchDto));
    }

    @Test
    void validateLocationSearchDto_shouldNotThrowException() {

        String name = "gasometer";
        String city = "wien";
        String zip = "zip";
        String country = "aut";
        String street = "test";

        LocationSearchDto searchDto = new LocationSearchDto();
        searchDto.setName(name);
        searchDto.setCountry(country);
        searchDto.setCity(city);
        searchDto.setZipCode(zip);
        searchDto.setStreet(street);

    }

}
