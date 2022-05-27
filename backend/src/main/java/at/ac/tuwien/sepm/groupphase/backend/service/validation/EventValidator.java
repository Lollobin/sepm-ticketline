package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Component
public class EventValidator {

    public void checkIfEvenIsValid(Event event) {

        String exceptionString = "";
        boolean first = true;

        if (event.getName().isBlank()) {
            exceptionString += "Name of event can not be empty ";
            first = false;
        }

        if (isStringLengthInvalid(event.getName())) {
            if (!first) {
                exceptionString += "& ";
            }
            exceptionString += "Name of event is too long ";
            first = false;
        }



        if (isDurationInvalid(event.getDuration())) {
            if (!first) {
                exceptionString += "& ";
            }
            exceptionString += "Duration has to be at least 10 minutes and less than 6 hours (360 minutes)";
            first = false;
        }

        if (!first) {
            throw new ValidationException(exceptionString);
        }
    }

    private boolean isStringLengthInvalid(String toCheck) {
        return toCheck.getBytes(StandardCharsets.UTF_8).length > 255;
    }

    private boolean isDurationInvalid(Long duration) {
        return duration < 10 || duration > 360;
    }

}
