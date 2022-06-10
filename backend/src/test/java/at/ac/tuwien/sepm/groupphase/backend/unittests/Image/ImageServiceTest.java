package at.ac.tuwien.sepm.groupphase.backend.unittests.Image;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ImageServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ImageValidator;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ImageServiceTest {

    @Mock
    private FileSystemRepository fileSystemRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageValidator imageValidator;

    private ImageService imageService;

    @BeforeEach
    public void setUp(){
        imageService = new ImageServiceImpl(fileSystemRepository, imageRepository, imageValidator);
    }


    @Test
    void save_shouldCallCorrectMethods() throws IOException {
        MultipartFile file = new MockMultipartFile("test.txt", "hello world".getBytes());

        Image fakePersistedImage = new Image();

        Image returnedImage = new Image();
        returnedImage.setImageId(1L);
        returnedImage.setFilePath("testFilePath");




        when(fileSystemRepository.save(file.getBytes(), file.getOriginalFilename())).thenReturn(
            "testFilePath");
        fakePersistedImage.setFilePath("testFilePath");
        when(imageRepository.save(fakePersistedImage)).thenReturn(returnedImage);


        imageService.save(file);

        verify(imageValidator).checkIfImageMimeTypeIsCorrect(file);
        verify(fileSystemRepository).save(file.getBytes(), file.getOriginalFilename());
        verify(imageRepository).save(fakePersistedImage);

    }

}
