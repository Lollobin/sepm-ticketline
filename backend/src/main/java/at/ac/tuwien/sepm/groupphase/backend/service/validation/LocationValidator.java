package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class LocationValidator {

    private final AddressValidator addressValidator;

    public LocationValidator(AddressValidator addressValidator) {
        this.addressValidator = addressValidator;
    }

    public void checkLocationIsValid(Location location) {
        if (Objects.equals(location.getName(), null) && location.getName().isBlank()) {
            throw new ValidationException(
                "The location must have a name that is longer than 0 characters long");
        }
        if (Objects.equals(location.getAddress(), null)) {
            throw new ValidationException(
                "The location must have an address");
        }
        addressValidator.validateAddress(location.getAddress());
    }
}
