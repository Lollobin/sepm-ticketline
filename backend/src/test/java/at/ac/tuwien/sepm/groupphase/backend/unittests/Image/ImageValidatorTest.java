package at.ac.tuwien.sepm.groupphase.backend.unittests.Image;

import static org.junit.jupiter.api.Assertions.assertThrows;

import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ImageValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class ImageValidatorTest {

    private ImageValidator imageValidator;

    @BeforeEach
    void setUp() {
        this.imageValidator = new ImageValidator();
    }

    @Test
    void checkIfImageMimeTypeIsCorrect_withInvalidMimeType_shouldThrowException() {
        MultipartFile file = new MockMultipartFile("wrong", "", MediaType.APPLICATION_JSON_VALUE,
            "test/image.data".getBytes());

        assertThrows(ValidationException.class,
            () -> imageValidator.checkIfImageMimeTypeIsCorrect(file));
    }

    @Test
    void checkIfImageMimeTypeIsCorrect_withValidMimeType_shouldNotThrowException() {
        MultipartFile file = new MockMultipartFile("wrong", "", MediaType.IMAGE_PNG_VALUE,
            "test/image.data".getBytes());

        imageValidator.checkIfImageMimeTypeIsCorrect(file);

    }

}

