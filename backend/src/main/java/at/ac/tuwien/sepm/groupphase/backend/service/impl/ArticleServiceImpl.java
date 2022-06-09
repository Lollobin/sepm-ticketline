package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArticleMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArticleRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ImageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArticleService;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.validation.ArticleValidator;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public ArticleServiceImpl(ArticleRepository articleRepository,
        ImageService imageService, ImageRepository imageRepository, ArticleMapper articleMapper,
        ArticleValidator articleValidator) {
        this.articleRepository = articleRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.articleMapper = articleMapper;
        this.articleValidator = articleValidator;
    }

    @Override
    public void createNewsArticle(ArticleWithoutIdDto articleWithoutIdDto) {

        LOGGER.trace("creating new article");

        articleValidator.checkIfArticleIsValid(articleWithoutIdDto, imageRepository);

        Article articleToUpdateImage = articleRepository.save(
            articleMapper.articleDtoToArticle(articleWithoutIdDto));

        for (long i : articleWithoutIdDto.getImages()) {
            imageService.updateArticle(i, articleToUpdateImage);
        }


    }

}
