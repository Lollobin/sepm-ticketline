package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ArticleValidator {


    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public void checkIfArticleIsValid(ArticleWithoutIdDto article,
        ImageRepository imageRepository) {

        LOGGER.trace("Validation of article");

        String exceptionString = "";
        boolean first = true;

        if (article.getTitle().isBlank()) {
            exceptionString += "Title of article can not be empty ";
            first = false;
        }

        if (isStringLengthInvalid(article.getTitle())) {
            if (!first) {
                exceptionString += "& ";
            }
            exceptionString += "Title of event is too long ";
            first = false;
        }

        for (long id : article.getImages()) {
            if (!imageRepository.existsById(id)) {
                if (!first) {
                    exceptionString += "& ";
                }
                exceptionString += "Image with id " + id + " does not exist";
                first = false;
            }
        }

        if (!first) {
            throw new ValidationException(exceptionString);
        }


    }

    private boolean isStringLengthInvalid(String toCheck) {
        return toCheck.getBytes(StandardCharsets.UTF_8).length > 100;
    }

}
