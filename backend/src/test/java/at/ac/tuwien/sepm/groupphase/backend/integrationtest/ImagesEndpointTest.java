package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLES_BASE_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ImagesEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
    }

    @Test
    void postWithCorrectImage_shouldReturn201() throws Exception {
//        MockMultipartFile file = new MockMultipartFile("image", "", "application/json", "test/image.data".getBytes());
//
//        MvcResult mvcResult = this.mockMvc
//            .perform(MockMvcRequestBuilders.multipart("/images").file(file).header(
//                    securityProperties.getAuthHeader(),
//                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))
//                ).andReturn();
//        MockHttpServletResponse response = mvcResult.getResponse();
//
//        assertEquals(response.getStatus(), HttpStatus.CREATED.value());

        MockMultipartFile firstFile = new MockMultipartFile("image", "test_image.png", MediaType.IMAGE_PNG_VALUE, "<<image data>>".getBytes(
            StandardCharsets.UTF_8));


        mockMvc.perform(multipart("/images").file(firstFile).accept(MediaType.IMAGE_PNG).header(
                    securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))).andExpect(status().isCreated()).andReturn();
    }

}
