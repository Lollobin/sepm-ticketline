package at.ac.tuwien.sepm.groupphase.backend.validator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

@Component
public class ShowValidator {

    public void checkIfShowCorrect(Show show) {
        if (!show.getDate().isAfter(OffsetDateTime.now())) {
            throw new ValidationException("The show date has to be in the future");
        }
    }

}
