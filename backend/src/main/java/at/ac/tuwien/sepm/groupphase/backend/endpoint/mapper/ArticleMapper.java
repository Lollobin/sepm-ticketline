package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArticleWithoutIdDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Article;
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

    ArticleDto articleToArticleDto(Article article);
}
