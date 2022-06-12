package at.ac.tuwien.sepm.groupphase.backend.unittests.article;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArticleMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArticleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.ArticleServiceImpl;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ArticleValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ArticleMapper articleMapper;
    @Mock
    private ArticleValidator articleValidator;

    @Mock
    private UserService userService;

    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        this.articleService = new ArticleServiceImpl(articleRepository, imageService,
            imageRepository, articleMapper, articleValidator, userService);
    }

    @Test
    void getArticlesFilterReadFalse_verifyMethods() {

        String email = "test";

        List<Article> articleList = new ArrayList<>();
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1L);

        when(userService.findApplicationUserByEmail(email)).thenReturn(user);
        when(articleRepository.findArticlesReadByUser(1L)).thenReturn(articleList);

        articleService.getArticles(true, email);

        verify(articleRepository).findArticlesReadByUser(1L);
        verify(userService).findApplicationUserByEmail(email);
        verify(articleRepository, never()).findArticlesNotReadByUser(1L);

    }

    @Test
    void getArticlesFilterReadTrue_verifyMethods() {

        String email = "test";

        List<Article> articleList = new ArrayList<>();
        ApplicationUser user = new ApplicationUser();
        user.setUserId(1L);

        when(userService.findApplicationUserByEmail(email)).thenReturn(user);
        when(articleRepository.findArticlesNotReadByUser(1L)).thenReturn(articleList);

        articleService.getArticles(false, email);

        verify(articleRepository).findArticlesNotReadByUser(1L);
        verify(userService).findApplicationUserByEmail(email);
        verify(articleRepository, never()).findArticlesReadByUser(1L);

    }

    @Test
    void getArticleDtoById_verifyMethods() {

        Article article = new Article();
        article.setArticleId(1L);
        Optional<Article> optionalArticle = Optional.of(article);

        when(articleRepository.findById(1L)).thenReturn(optionalArticle);

        articleService.getArticleDtoById(1L);

        verify(articleRepository).findById(1L);


    }


}
