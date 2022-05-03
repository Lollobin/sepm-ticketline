package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;


@Component
public class AddressValidator {


    public void validateAddress(AddressDto address) {
        validateStreet(address.getStreet());
        validateCountry(address.getCountry());
        validateCity(address.getCity());
        validateHouseNo(address.getHouseNumber());
        validateZipCode(address.getZipCode());
    }

    private void validateZipCode(String zipCode) {
        try{
            validateNotEmpty(zipCode);
        } catch (ValidationException e){
            throw new ValidationException ("Check Zip Code! "+ e.getMessage());
        }
    }

    private void validateHouseNo(String houseNumber) {
        try{
            validateNotEmpty(houseNumber);
        } catch (ValidationException e){
            throw new ValidationException ("Check House Number! "+ e.getMessage());
        }
    }

    private void validateCountry(String country) {
        try{
            validateNotEmpty(country);
        } catch (ValidationException e){
            throw new ValidationException ("Check Country! "+ e.getMessage());
        }
        if (!Pattern.matches("([A-Za-z0-9_äÄöÖüÜß])+", country)){
            throw new ValidationException("Not a valid country!");
        }

    }

    private void validateStreet(String street) {
        try{
            validateNotEmpty(street);
        } catch (ValidationException e){
            throw new ValidationException ("Check Street! "+ e.getMessage());
        }
    }

    private void validateCity(String city) {
        try{
            validateNotEmpty(city);
        } catch (ValidationException e){
            throw new ValidationException ("Check City! "+ e.getMessage());
        }
    }

    private void validateNotEmpty(String s){
        if(s.trim().length()==0) throw new ValidationException("This field cannot be empty or contain only spaces!");
    }
}
