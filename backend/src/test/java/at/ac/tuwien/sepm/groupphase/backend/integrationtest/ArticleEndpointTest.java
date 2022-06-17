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
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.FileSystemRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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

    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Autowired
    private ImageRepository imageRepository;


    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql(value = "classpath:/sql/delete.sql", executionPhase = BEFORE_TEST_METHOD)})
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
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql(value = "classpath:/sql/delete.sql", executionPhase = BEFORE_TEST_METHOD)})
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

            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatus());

    }

    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql(value = "classpath:/sql/delete.sql", executionPhase = BEFORE_TEST_METHOD)})
    void postWithOneImage_shouldReturn201() throws Exception {
        articleRepository.deleteAll();
        imageRepository.deleteAll();

        String fakeImage = "das ist ein Teststring";
        String filePath = fileSystemRepository.save(fakeImage.getBytes(StandardCharsets.UTF_8),
            fakeImage);

        Image image = new Image();
        image.setFilePath(filePath);

        imageRepository.save(image);

        Long id = imageRepository.findAll().get(0).getImageId();

        ArticleWithoutIdDto articleWithoutIdDto = new ArticleWithoutIdDto();

        articleWithoutIdDto.setTitle(ARTICLE_TITLE);
        articleWithoutIdDto.setSummary(ARTICLE_SUMMARY);
        articleWithoutIdDto.setText(ARTICLE_TEXT);
        List<Long> imageIds = new ArrayList<>();
        imageIds.add(id);
        articleWithoutIdDto.setImages(imageIds);

        String json = objectMapper.writeValueAsString(articleWithoutIdDto);

        MvcResult mvcResult = this.mockMvc
            .perform(
                post(ARTICLES_BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        List<Article> persistedArticles = articleRepository.findAll();
        List<Image> persistedImages = imageRepository.findAll();

        assertThat(persistedArticles).hasSize(1);
        assertThat(persistedImages).hasSize(1);

        Article article = persistedArticles.get(0);

        assertAll(
            () -> assertEquals(ARTICLE_SUMMARY, article.getSummary()),
            () -> assertEquals(ARTICLE_TITLE, article.getTitle()),
            () -> assertEquals(ARTICLE_TEXT, article.getText())
        );

        assertThat(persistedImages.get(0).getArticle().getArticleId()).isEqualTo(
            article.getArticleId());
    }

    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql(value = "classpath:/sql/delete.sql", executionPhase = BEFORE_TEST_METHOD)})
    void postWithMultipleImages_shouldReturn201() throws Exception {

        imageRepository.deleteAll();
        articleRepository.deleteAll();

        String fakeImage = "image1";
        String filePath = fileSystemRepository.save(fakeImage.getBytes(StandardCharsets.UTF_8),
            fakeImage);

        String fakeImage2 = "image2";
        String filePath2 = fileSystemRepository.save(fakeImage2.getBytes(StandardCharsets.UTF_8),
            fakeImage2);

        String fakeImage3 = "image3";
        String filePath3 = fileSystemRepository.save(fakeImage3.getBytes(StandardCharsets.UTF_8),
            fakeImage3);

        Image image = new Image();
        image.setFilePath(filePath);

        Image image2 = new Image();
        image2.setFilePath(filePath2);

        Image image3 = new Image();
        image3.setFilePath(filePath3);

        imageRepository.save(image);
        imageRepository.save(image2);
        imageRepository.save(image3);

        Long id = imageRepository.findAll().get(0).getImageId();
        Long id2 = imageRepository.findAll().get(1).getImageId();
        Long id3 = imageRepository.findAll().get(2).getImageId();

        ArticleWithoutIdDto articleWithoutIdDto = new ArticleWithoutIdDto();

        articleWithoutIdDto.setTitle(ARTICLE_TITLE);
        articleWithoutIdDto.setSummary(ARTICLE_SUMMARY);
        articleWithoutIdDto.setText(ARTICLE_TEXT);
        List<Long> imageIds = new ArrayList<>();
        imageIds.add(id);
        imageIds.add(id2);
        imageIds.add(id3);
        articleWithoutIdDto.setImages(imageIds);

        String json = objectMapper.writeValueAsString(articleWithoutIdDto);

        MvcResult mvcResult = this.mockMvc
            .perform(
                post(ARTICLES_BASE_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
                    .header(
                        securityProperties.getAuthHeader(),
                        jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))

            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        List<Article> persistedArticles = articleRepository.findAll();
        List<Image> persistedImages = imageRepository.findAll();

        assertThat(persistedArticles).hasSize(1);
        assertThat(persistedImages).hasSize(3);

        Article article = persistedArticles.get(0);

        assertAll(
            () -> assertEquals(ARTICLE_SUMMARY, article.getSummary()),
            () -> assertEquals(ARTICLE_TITLE, article.getTitle()),
            () -> assertEquals(ARTICLE_TEXT, article.getText())
        );

        assertThat(persistedImages.get(0).getArticle().getArticleId()).isEqualTo(
            article.getArticleId());
        assertThat(persistedImages.get(1).getArticle().getArticleId()).isEqualTo(
            article.getArticleId());
        assertThat(persistedImages.get(2).getArticle().getArticleId()).isEqualTo(
            article.getArticleId());
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
    void articlesIdGetWithInvalidId_shouldReturn404() throws Exception {

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/articles/" + 100)
                .header(securityProperties.getAuthHeader(),
                    jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES))

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        assertEquals(servletResponse.getStatus(), HttpStatus.NOT_FOUND.value());

    }


    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),}
    )
    void articlesIdGetWithValidID_shouldReturnCorrectArticle() throws Exception {

        String name = "das ist ein Teststring";
        String filePath = fileSystemRepository.save(name.getBytes(StandardCharsets.UTF_8), name);

        Image image = new Image();
        image.setFilePath(filePath);

        String name2 = "das ist ein zweiter Teststring";
        String filePath2 = fileSystemRepository.save(name2.getBytes(StandardCharsets.UTF_8), name2);

        Image image2 = new Image();
        image2.setFilePath(filePath2);

        imageRepository.save(image);
        imageRepository.save(image2);

        List<Image> images = imageRepository.findAll();
        Article article = new Article();
        article.setText(ARTICLE_TEXT);
        article.setTitle(ARTICLE_TITLE);
        article.setSummary(ARTICLE_SUMMARY);
        article.setImages(images);
        article.setCreationDate(OffsetDateTime.now());

        articleRepository.save(article);

        assertThat(imageRepository.findAll()).hasSize(2);
        assertThat(articleRepository.findAll()).hasSize(1);
        Long id = articleRepository.findAll().get(0).getArticleId();

        MvcResult result = this.mockMvc.perform(
            MockMvcRequestBuilders.get("/articles/" + id)

        ).andReturn();

        MockHttpServletResponse servletResponse = result.getResponse();

        List<Article> articleList = articleRepository.findAll();

        ArticleDto articleDto = objectMapper.readValue(
            servletResponse.getContentAsString(), ArticleDto.class);

        assertThat(articleDto.getImages()).hasSize(2);

        assertAll(
            () -> assertEquals(articleDto.getArticleId(), id),
            () -> assertEquals(articleDto.getImages().get(0), images.get(0).getImageId()),
            () -> assertEquals(articleDto.getImages().get(1), images.get(1).getImageId())
        );
    }

}
