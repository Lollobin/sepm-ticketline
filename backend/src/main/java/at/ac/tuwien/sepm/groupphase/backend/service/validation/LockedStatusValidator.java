package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class LockedStatusValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public void isBodyNull(Boolean body) {

        LOGGER.debug("body has value {}", body);

        if (body == null) {
            throw new ValidationException("Body for locking must be either true or false");
        }
    }

}
