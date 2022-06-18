package at.ac.tuwien.sepm.groupphase.backend.unittests.Article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

@DataJpaTest
@ActiveProfiles("test")
class ArticleRepositoryTest {


    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql("classpath:/sql/insert_address.sql"), @Sql("classpath:/sql/insert_user.sql"),
        @Sql("classpath:/sql/insert_article.sql"),
        @Sql("classpath:/sql/insert_read_articles.sql")})
    void findArticlesReadByUser_shouldGetAllReadArticles() {

        List<Article> articlesReadByUser1 = articleRepository.findArticlesReadByUser(-1, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser1).hasSize(2);
        assertAll(
            () -> assertEquals(articlesReadByUser1.get(0).getArticleId(), -3),
            () -> assertEquals(articlesReadByUser1.get(1).getArticleId(), -1)
        );

        List<Article> articlesReadByUser2 = articleRepository.findArticlesReadByUser(-2, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser2).hasSize(1);
        assertEquals(articlesReadByUser2.get(0).getArticleId(), -1);

        List<Article> articlesReadByUser3 = articleRepository.findArticlesReadByUser(-3, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser3).hasSize(3);
        assertAll(
            () -> assertEquals(articlesReadByUser3.get(0).getArticleId(), -3),
            () -> assertEquals(articlesReadByUser3.get(1).getArticleId(), -2),
            () -> assertEquals(articlesReadByUser3.get(2).getArticleId(), -1)
        );

        List<Article> articlesReadByUser4 = articleRepository.findArticlesReadByUser(-4, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser4).hasSize(1);
        assertEquals(articlesReadByUser4.get(0).getArticleId(), -1);

        List<Article> articlesReadByUser5 = articleRepository.findArticlesReadByUser(-5, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser5).isEmpty();
    }

    @Test
    @SqlGroup({@Sql(value = "classpath:/sql/delete.sql", executionPhase = AFTER_TEST_METHOD),
        @Sql("classpath:/sql/insert_address.sql"), @Sql("classpath:/sql/insert_user.sql"),
        @Sql("classpath:/sql/insert_article.sql"),
        @Sql("classpath:/sql/insert_read_articles.sql")})
    void findArticlesNotReadByUser_shouldGetAllNotReadArticles() {

        List<Article> articlesReadByUser1 = articleRepository.findArticlesNotReadByUser(-1, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser1).hasSize(2);
        assertAll(
            () -> assertEquals(articlesReadByUser1.get(0).getArticleId(), -4),
            () -> assertEquals(articlesReadByUser1.get(1).getArticleId(), -2)
        );

        List<Article> articlesReadByUser2 = articleRepository.findArticlesNotReadByUser(-2, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser2).hasSize(3);
        assertAll(
            () -> assertEquals(articlesReadByUser2.get(0).getArticleId(), -4),
            () -> assertEquals(articlesReadByUser2.get(1).getArticleId(), -3),
            () -> assertEquals(articlesReadByUser2.get(2).getArticleId(), -2)
        );

        List<Article> articlesReadByUser3 = articleRepository.findArticlesNotReadByUser(-3, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser3).hasSize(1);
        assertEquals(articlesReadByUser3.get(0).getArticleId(), -4);

        List<Article> articlesReadByUser4 = articleRepository.findArticlesNotReadByUser(-4, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser4).hasSize(3);
        assertAll(
            () -> assertEquals(articlesReadByUser4.get(0).getArticleId(), -4),
            () -> assertEquals(articlesReadByUser4.get(1).getArticleId(), -3),
            () -> assertEquals(articlesReadByUser4.get(2).getArticleId(), -2)
        );

        List<Article> articlesReadByUser5 = articleRepository.findArticlesNotReadByUser(-5, Pageable.unpaged()).getContent();
        assertThat(articlesReadByUser5).hasSize(4);
        assertAll(
            () -> assertEquals(articlesReadByUser5.get(0).getArticleId(), -4),
            () -> assertEquals(articlesReadByUser5.get(1).getArticleId(), -3),
            () -> assertEquals(articlesReadByUser5.get(2).getArticleId(), -2),
            () -> assertEquals(articlesReadByUser5.get(3).getArticleId(), -1)
        );

    }
}





