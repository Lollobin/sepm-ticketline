package at.ac.tuwien.sepm.groupphase.backend.unittests.Article;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLE_SUMMARY;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLE_TEXT;
import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.ARTICLE_TITLE;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArticleMapper;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Spy
    private ArticleMapper articleMapper;

    @Mock
    private ImageRepository imageRepository;


    @Mock
    private ImageService imageService;

    @Mock
    private ArticleValidator articleValidator;

    @Mock
    private UserService userService;

    private ArticleService articleService;


    @BeforeEach
    void setUp() {

        articleService = new ArticleServiceImpl(articleRepository, imageService, imageRepository,
            articleMapper, articleValidator, userService);
    }

    @Test
    void createNewsArticleWithoutImage_shouldCallCorrectMethods() {
        ArticleWithoutIdDto articleWithoutIdDto = new ArticleWithoutIdDto();

        articleWithoutIdDto.setTitle(ARTICLE_TITLE);
        articleWithoutIdDto.setSummary(ARTICLE_SUMMARY);
        articleWithoutIdDto.setText(ARTICLE_TEXT);

        Article fakedPersisted = new Article();
        fakedPersisted.setArticleId(1L);
        fakedPersisted.setTitle(ARTICLE_TITLE);
        fakedPersisted.setSummary(ARTICLE_SUMMARY);
        fakedPersisted.setText(ARTICLE_TEXT);

        when(articleRepository.save(fakedPersisted)).thenReturn(fakedPersisted);
        when(articleMapper.articleDtoToArticle(articleWithoutIdDto)).thenReturn(fakedPersisted);

        articleService.createNewsArticle(articleWithoutIdDto);

        verify(articleMapper).articleDtoToArticle(articleWithoutIdDto);
        verify(articleValidator).checkIfArticleIsValid(articleWithoutIdDto, imageRepository);
        verify(articleRepository, times(1)).save(fakedPersisted);
        verify(imageService, times(0)).updateArticle(1L, fakedPersisted);


    }

    @Test
    void createNewsArticleWithImage_shouldCallCorrectMethods() {
        ArticleWithoutIdDto articleWithoutIdDto = new ArticleWithoutIdDto();

        List<Long> imageIds = new ArrayList<>();
        imageIds.add(1L);
        imageIds.add(2L);

        articleWithoutIdDto.setTitle(ARTICLE_TITLE);
        articleWithoutIdDto.setSummary(ARTICLE_SUMMARY);
        articleWithoutIdDto.setText(ARTICLE_TEXT);
        articleWithoutIdDto.setImages(imageIds);

        Article fakedPersisted = new Article();
        fakedPersisted.setArticleId(1L);
        fakedPersisted.setTitle(ARTICLE_TITLE);
        fakedPersisted.setSummary(ARTICLE_SUMMARY);
        fakedPersisted.setText(ARTICLE_TEXT);

        when(articleRepository.save(fakedPersisted)).thenReturn(fakedPersisted);
        when(articleMapper.articleDtoToArticle(articleWithoutIdDto)).thenReturn(fakedPersisted);

        articleService.createNewsArticle(articleWithoutIdDto);

        verify(articleMapper).articleDtoToArticle(articleWithoutIdDto);
        verify(articleValidator).checkIfArticleIsValid(articleWithoutIdDto, imageRepository);
        verify(articleRepository, times(1)).save(fakedPersisted);
        verify(imageService, times(2)).updateArticle(anyLong(), eq(fakedPersisted));

    }

}
