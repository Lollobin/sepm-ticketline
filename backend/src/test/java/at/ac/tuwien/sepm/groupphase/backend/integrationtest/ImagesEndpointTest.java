package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ImagesEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Autowired
    private ImageRepository imageRepository;

    @BeforeEach
    void setUp() {
        imageRepository.deleteAll();
    }

    @Test
    void imagesIdGet_shouldReturnCorrectImage() throws Exception {
        String name = "das ist ein Teststring";
        String filePath = fileSystemRepository.save(name.getBytes(StandardCharsets.UTF_8), name);

        Image image = new Image();
        image.setFilePath(filePath);

        imageRepository.save(image);

        Image image1 = imageRepository.findAll().get(0);

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/images/" + image1.getImageId())

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        assertEquals(servletResponse.getStatus(), HttpStatus.OK.value());

    }

    @Test
    void imagesIdGetWithInvalidId_shouldReturn404() throws Exception {
        String name = "das ist ein Teststring";
        String filePath = fileSystemRepository.save(name.getBytes(StandardCharsets.UTF_8), name);

        Image image = new Image();
        image.setFilePath(filePath);

        imageRepository.save(image);

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/images/"  + 100)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        assertEquals(servletResponse.getStatus(), HttpStatus.NOT_FOUND.value());

    }

}
