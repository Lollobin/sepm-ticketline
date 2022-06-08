package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class AddressValidator {

    public void validateAddress(AddressDto address) {
        validateStreet(address.getStreet());
        validateCountry(address.getCountry());
        validateCity(address.getCity());
        validateHouseNo(address.getHouseNumber());
        validateZipCode(address.getZipCode());
    }

    public void validateAddress(Address address) {
        validateStreet(address.getStreet());
        validateCountry(address.getCountry());
        validateCity(address.getCity());
        validateHouseNo(address.getHouseNumber());
        validateZipCode(address.getZipCode());
    }

    private void validateZipCode(String zipCode) {
        if (zipCode.length() > 16) {
            throw new ValidationException("ZipCode must be at most 16 characters long");
        }
        validateNotEmpty(zipCode, "Check Zip Code! ");
    }

    private void validateHouseNo(String houseNumber) {
        if (houseNumber.length() > 40) {
            throw new ValidationException("houseNumber must be at most 16 characters long");
        }
        validateNotEmpty(houseNumber, "Check House number! ");
    }

    private void validateCountry(String country) {
        if (country.length() > 100) {
            throw new ValidationException("country must be at most 16 characters long");
        }
        validateNotEmpty(country, "Check Country! ");
        if (!Pattern.matches("([A-Za-z0-9_äÄöÖüÜß])+", country)) {
            throw new ValidationException("Not a valid country!");
        }
    }

    private void validateStreet(String street) {
        if (street.length() > 100) {
            throw new ValidationException("street must be at most 16 characters long");
        }
        validateNotEmpty(street, "Check Street! ");
    }

    private void validateCity(String city) {
        if (city.length() > 100) {
            throw new ValidationException("city must be at most 16 characters long");
        }
        validateNotEmpty(city, "Check City! ");
    }

    private void validateNotEmpty(String s, String message) {
        if (s.trim().length() == 0) {
            throw new ValidationException(
                message + "This field cannot be empty or contain only spaces!");
        }
    }
}
