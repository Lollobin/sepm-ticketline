package at.ac.tuwien.sepm.groupphase.backend.validator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Show;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ShowValidator {

    public void checkIfShowCorrect(Show show) {
        if (!show.getDate().isAfter(LocalDate.now())) {
            throw new ValidationException("The show can not be in the past");
        }
    }

}
