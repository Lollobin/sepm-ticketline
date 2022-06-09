package at.ac.tuwien.sepm.groupphase.backend.service.validation;

import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());

    public void checkIfImageMimeTypeIsCorrect(MultipartFile multipartFile) {

        String mimeType = multipartFile.getContentType();

        LOGGER.debug("mimeType of file: {}", mimeType);

        if (mimeType == null || !mimeType.split("/")[0].equals("image")) {
            throw new ValidationException("The file has to be an image");
        }

    }
}
