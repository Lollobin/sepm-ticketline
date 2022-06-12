package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_ROLES;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ADMIN_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ArticleEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;


    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql("classpath:/sql/insert_address.sql"), @Sql("classpath:/sql/insert_user.sql"),
        @Sql("classpath:/sql/insert_article.sql"),
        @Sql("classpath:/sql/insert_read_articles.sql")})
    void articlesGetWithFilterReadFalse_shouldReturnNotReadArticles() throws Exception {

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/articles").param("filterRead", String.valueOf(false))
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        List<Article> articleList = Arrays.asList(objectMapper.readValue(
            servletResponse.getContentAsString(), Article[].class));

        assertThat(articleList).hasSize(2);
        assertAll(
            () -> assertEquals(articleList.get(0).getArticleId(), -4),
            () -> assertEquals(articleList.get(1).getArticleId(), -2)
        );
    }

    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql("classpath:/sql/insert_address.sql"), @Sql("classpath:/sql/insert_user.sql"),
        @Sql("classpath:/sql/insert_article.sql"),
        @Sql("classpath:/sql/insert_read_articles.sql")})
    void articlesGetWithFilterReadTrue_shouldReturnReadArticles() throws Exception {

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/articles").param("filterRead", String.valueOf(true))
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        List<Article> articleList = Arrays.asList(objectMapper.readValue(
            servletResponse.getContentAsString(), Article[].class));

        assertThat(articleList).hasSize(2);
        assertAll(
            () -> assertEquals(articleList.get(0).getArticleId(), -3),
            () -> assertEquals(articleList.get(1).getArticleId(), -1)
        );
    }

    @Test
    void imagesIdGetWithInvalidId_shouldReturn404() throws Exception {

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/articles/" + 100)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        assertEquals(servletResponse.getStatus(), HttpStatus.NOT_FOUND.value());

    }

}
