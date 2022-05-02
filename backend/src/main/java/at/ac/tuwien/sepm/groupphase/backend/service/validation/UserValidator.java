package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserWithPasswordDto;
import org.springframework.stereotype.Component;

import javax.validation.ValidationException;
import java.util.regex.Pattern;

@Component
public class UserValidator {

    private final AddressValidator addressValidator;

    public UserValidator(AddressValidator addressValidator) {
        this.addressValidator = addressValidator;
    }

    public void validateUserWithPasswordDto(UserWithPasswordDto user){
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        addressValidator.validateAddress(user.getAddress());
    }

    private void validatePassword(String password) {
        if(password.length()<8) throw new ValidationException("Password too short");
    }

    private void validateEmail(String email) {
        String m;
        if (email != null) {
            if (email.length() > 254) {
                m = "Mail must be at max 254 characters long!";
                throw new ValidationException(m);
            }

            String validMailPattern = "^(.+)@(.+)$";
            if (!Pattern.matches(validMailPattern, email)) {
                m = "Given input is not a valid mail address!";
                throw new ValidationException(m);
            }

        }



    }



}
