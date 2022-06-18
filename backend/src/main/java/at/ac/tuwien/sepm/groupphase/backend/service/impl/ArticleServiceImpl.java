package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticlePageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArticleMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArticleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ArticleValidator;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl implements ArticleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());
    private final ArticleRepository articleRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    private final ArticleMapper articleMapper;
    private final ArticleValidator articleValidator;

    private final UserService userService;

    public ArticleServiceImpl(ArticleRepository articleRepository,
        ImageService imageService, ImageRepository imageRepository, ArticleMapper articleMapper,
        ArticleValidator articleValidator, UserService userService) {
        this.articleRepository = articleRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.articleMapper = articleMapper;
        this.articleValidator = articleValidator;
        this.userService = userService;
    }

    @Override
    public long createNewsArticle(ArticleWithoutIdDto articleWithoutIdDto) {

        LOGGER.trace("creating new article");

        articleValidator.checkIfArticleIsValid(articleWithoutIdDto, imageRepository);

        Article mapped = articleMapper.articleDtoToArticle(articleWithoutIdDto);

        mapped.setImages(imageRepository.findAllById(articleWithoutIdDto.getImages()));

        Article articleToUpdateImage = articleRepository.save(mapped);

        for (long i : articleWithoutIdDto.getImages()) {
            imageService.updateArticle(i, articleToUpdateImage);
        }

        return articleToUpdateImage.getArticleId();


    }


    @Override
    public ArticleDto getArticleDtoById(Long id) {

        LOGGER.trace("Getting article with id {}", id);

        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (optionalArticle.isEmpty()) {
            throw new NotFoundException("Article with id " + id + " not found");
        }
        return articleMapper.articleToArticleDto(optionalArticle.get());

    }

    @Override
    public ArticlePageDto getArticles(Boolean filterRead, String email, boolean isAnonym,
        Pageable pageable) {
        LOGGER.trace("Getting articles");

        Long id = null;

        if (!isAnonym) {
            id = userService.findApplicationUserByEmail(email).getUserId();
        }



        if (isAnonym) {
            Page<Article> articlePage = articleRepository.findAll(pageable);
            return setArticlePageDto(articlePage);


        } else if (Boolean.FALSE.equals(filterRead)) {
            Page<Article> articlePage = articleRepository.findArticlesNotReadByUser(id, pageable);
            return setArticlePageDto(articlePage);
        } else {
            Page<Article> articlePage = articleRepository.findArticlesReadByUser(id, pageable);
            return setArticlePageDto(articlePage);

        }
    }

    private ArticlePageDto setArticlePageDto(Page<Article> articlePage) {
        LOGGER.trace("Setting ArticlePageDto values");
        ArticlePageDto searchResultDto = new ArticlePageDto();

        searchResultDto.setArticles(
            articlePage.getContent().stream().map(articleMapper::articleToArticleDto).toList());
        searchResultDto.setNumberOfResults((int) articlePage.getTotalElements());
        searchResultDto.setCurrentPage(articlePage.getNumber());
        searchResultDto.setPagesTotal(articlePage.getTotalPages());

        return searchResultDto;
    }


}
