package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
import at.ac.tuwien.sepm.groupphase.backend.entity.Image;
import java.time.OffsetDateTime;
import org.mapstruct.Mapper;

@Mapper
public interface ArticleMapper {


    default Article articleDtoToArticle(ArticleWithoutIdDto articleWithoutIdDto) {

        Article article = new Article();

        article.setTitle(articleWithoutIdDto.getTitle());
        article.setText(articleWithoutIdDto.getText());
        article.setSummary(articleWithoutIdDto.getSummary());
        article.setCreationDate(OffsetDateTime.now());

        return article;
    }


    default ArticleDto articleToArticleDto(Article article) {
        ArticleDto articleDto = new ArticleDto();

        articleDto.setArticleId(article.getArticleId());
        articleDto.setCreationDate(article.getCreationDate());
        articleDto.setSummary(article.getSummary());
        articleDto.setText(article.getText());
        articleDto.setTitle(article.getTitle());
        articleDto.setImages(article.getImages().stream().map(Image::getImageId).toList());

        return articleDto;
    }
}
