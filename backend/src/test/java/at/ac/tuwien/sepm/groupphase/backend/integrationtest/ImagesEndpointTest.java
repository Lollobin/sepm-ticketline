package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
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
    private JwtTokenizer jwtTokenizer;


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Autowired
    private SecurityProperties securityProperties;

    @Test
    @Sql(value = "classpath:/sql/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void postWithCorrectImage_shouldReturn201() throws Exception {

        imageRepository.deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/images")
                .file(new MockMultipartFile("fileName", "testfile.jpg", MediaType.IMAGE_JPEG_VALUE,
                    "test/image.data".getBytes()))
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andExpect(status().isCreated());
    }

    @Test
    @Sql(value = "classpath:/sql/delete.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
    void postWithWrongRole_shouldReturn403() throws Exception {

        imageRepository.deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/images")
                .file(new MockMultipartFile("fileName", "testfile.jpg", MediaType.IMAGE_JPEG_VALUE,
                    "test/image.data".getBytes()))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    void postWithWrongRoleMimeType_shouldReturn422() throws Exception {

        imageRepository.deleteAll();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/images")
                .file(
                    new MockMultipartFile("fileName", "testfile.jpg", MediaType.APPLICATION_JSON_VALUE,
                        "test/image.data".getBytes())).header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            )
            .andExpect(status().isUnprocessableEntity());
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
