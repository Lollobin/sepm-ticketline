package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
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
    private SecurityProperties securityProperties;

    @BeforeEach
    void setUp() {
        imageRepository.deleteAll();
    }

    @Test
    void postWithCorrectImage_shouldReturn201() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/images")
                .file(new MockMultipartFile("fileName", "testfile.jpg", MediaType.IMAGE_JPEG_VALUE,
                    "test/image.data".getBytes()))
                .header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andExpect(status().isCreated());
    }

    @Test
    void postWithWrongRole_shouldReturn403() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/images")
                .file(new MockMultipartFile("fileName", "testfile.jpg", MediaType.IMAGE_JPEG_VALUE,
                    "test/image.data".getBytes()))
            )
            .andExpect(status().isForbidden());
    }

    @Test
    void postWithWrongRoleMimeType_shouldReturn422() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart("/images")
                .file(
                    new MockMultipartFile("fileName", "testfile.jpg", MediaType.APPLICATION_JSON_VALUE,
                        "test/image.data".getBytes())).header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
            )
            .andExpect(status().isUnprocessableEntity());
    }

}
