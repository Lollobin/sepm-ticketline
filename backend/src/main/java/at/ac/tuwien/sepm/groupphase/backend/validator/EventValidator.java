package at.ac.tuwien.sepm.groupphase.backend.validator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


@Component
public class EventValidator {

    public void checkIfEvenIsValid(Event event){

        String exceptionString = "";
        if(event.getName().isBlank() || isStringLengthInvalid(event.getName()) || isStringLengthInvalid(event.getCategory())){

            if(event.getName().isBlank()){
                exceptionString += "Name of event can not be empty ";
            }

            if(isStringLengthInvalid(event.getName())){
                exceptionString += "Name of event is too long ";
            }
            if(isStringLengthInvalid(event.getCategory())){
                exceptionString += "Category contains too many characters";
            }

            throw new ValidationException(exceptionString);

        }
    }

    private boolean isStringLengthInvalid(String toCheck){
        return toCheck.getBytes(StandardCharsets.UTF_8).length > 255;
    }

}
