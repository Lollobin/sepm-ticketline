package at.ac.tuwien.sepm.groupphase.backend.unittests.Article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ArticleValidator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ArticleValidatorTest {

    private ArticleValidator articleValidator;

    @Mock
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        this.articleValidator = new ArticleValidator();
    }

    @Test
    void checkIfArticleIsValid_withInvalidDto_shouldThrowException() {

        ArticleWithoutIdDto articleWithoutIdDto = new ArticleWithoutIdDto();
        articleWithoutIdDto.setTitle("   ");
        articleWithoutIdDto.setSummary("   ");
        articleWithoutIdDto.setText("   ");
        List<Integer> images = new ArrayList<>();
        images.add(1);

        articleWithoutIdDto.setImages(images);

        when(imageRepository.existsById(1L)).thenReturn(false);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, ()-> articleValidator.checkIfArticleIsValid(articleWithoutIdDto, imageRepository));

        String expectedMessage = "Title of article can not be empty & Image with id 1 does not exist";

        assertEquals(exception.getMessage(), expectedMessage);

    }

}
