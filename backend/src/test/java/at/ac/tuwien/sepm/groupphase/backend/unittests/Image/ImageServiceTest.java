package at.ac.tuwien.sepm.groupphase.backend.unittests.Image;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ImageServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ImageValidator;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageValidator imageValidator;
    @Mock
    private FileSystemRepository fileSystemRepository;
    @Mock
    private ImageRepository imageRepository;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        this.imageService = new ImageServiceImpl(fileSystemRepository, imageRepository,
            imageValidator);
    }

    @Test
    void getImageById_verifyMethods() {

        Image image = new Image();
        image.setImageId(1L);

        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        imageService.getImageById(1L);

        verify(imageRepository).findById(1L);

    }

}
