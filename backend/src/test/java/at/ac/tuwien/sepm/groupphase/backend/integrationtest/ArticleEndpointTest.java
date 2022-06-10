package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLES_BASE_URI;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLE_SUMMARY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLE_TEXT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLE_TITLE;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.USER_ROLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class ArticleEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
    }

    @Test
    void postWithoutImage_shouldSaveArticle() throws Exception {

        ArticleWithoutIdDto articleWithoutIdDto = new ArticleWithoutIdDto();

        articleWithoutIdDto.setTitle(ARTICLE_TITLE);
        articleWithoutIdDto.setSummary(ARTICLE_SUMMARY);
        articleWithoutIdDto.setText(ARTICLE_TEXT);

        String json = objectMapper.writeValueAsString(articleWithoutIdDto);

        MvcResult mvcResult = this.mockMvc
            .perform(
                post(ARTICLES_BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        List<Article> persistedArticles = articleRepository.findAll();

        assertThat(persistedArticles).hasSize(1);
        Article article = persistedArticles.get(0);

        assertAll(
            () -> assertEquals(ARTICLE_SUMMARY, article.getSummary()),
            () -> assertEquals(ARTICLE_TITLE, article.getTitle()),
            () -> assertEquals(ARTICLE_TEXT, article.getText())
        );
    }

    @Test
    void postWithInvalidRole_shouldReturn403() throws Exception {

        ArticleWithoutIdDto articleWithoutIdDto = new ArticleWithoutIdDto();

        articleWithoutIdDto.setTitle(ARTICLE_TITLE);
        articleWithoutIdDto.setSummary(ARTICLE_SUMMARY);
        articleWithoutIdDto.setText(ARTICLE_TEXT);

        String json = objectMapper.writeValueAsString(articleWithoutIdDto);

        MvcResult mvcResult = this.mockMvc
            .perform(
                post(ARTICLES_BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, USER_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());

    }
}
