package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class SearchValidator {

    public void validateLocationSearchDto(LocationSearchDto locationSearchDto) {

        String exceptionString = "";
        boolean first = true;

        if (isStringLengthInvalid(locationSearchDto.getName())) {

            exceptionString += "Name of event is too long ";
            first = false;
        }
        if (isStringLengthInvalid(locationSearchDto.getCountry())) {
            if (!first) {
                exceptionString += "& ";
            }

            exceptionString += "Country search param is too long";
            first = false;
        }
        if (isStringLengthInvalid(locationSearchDto.getCity())) {
            if (!first) {
                exceptionString += "& ";
            }
            exceptionString += "City search param is too long";
            first = false;
        }
        if (isStringLengthInvalid(locationSearchDto.getStreet())) {
            if (!first) {
                exceptionString += "& ";
            }

            exceptionString += "Street search param is too long";
            first = false;
        }
        if (isStringLengthInvalid(locationSearchDto.getZipCode())) {
            if (!first) {
                exceptionString += "& ";
            }
            exceptionString += "Zip code search param is too long";
            first = false;
        }

        if (!first) {
            throw new ValidationException(exceptionString);
        }


    }

    private boolean isStringLengthInvalid(String toCheck) {
        return toCheck.getBytes(StandardCharsets.UTF_8).length > 255;
    }

}
