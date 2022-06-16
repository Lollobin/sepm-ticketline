package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class LockedStatusValidator {

    public void isBodyNull(Boolean body) {

        if (body == null) {
            throw new ValidationException("Body for locking must be either true or false");
        }
    }

    public void isUserAdmin(ApplicationUser user) {

        if (user.isHasAdministrativeRights()) {
            throw new ValidationException("Administrators can not get locked");
        }
    }
}
