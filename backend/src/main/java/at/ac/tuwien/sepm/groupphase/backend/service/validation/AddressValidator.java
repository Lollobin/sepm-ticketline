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
        validateNotEmpty(zipCode, "Check Zip Code! ");
    }

    private void validateHouseNo(String houseNumber) {
        validateNotEmpty(houseNumber, "Check House number! ");
    }

    private void validateCountry(String country) {
        validateNotEmpty(country, "Check Country! ");
        if (!Pattern.matches("([A-Za-z0-9_äÄöÖüÜß])+", country)) {
            throw new ValidationException("Not a valid country!");
        }
    }

    private void validateStreet(String street) {
        validateNotEmpty(street, "Check Street! ");
    }

    private void validateCity(String city) {
        validateNotEmpty(city, "Check City! ");
    }

    private void validateNotEmpty(String s, String message) {
        if (s.trim().length() == 0) {
            throw new ValidationException(
                message + "This field cannot be empty or contain only spaces!");
        }
    }
}
